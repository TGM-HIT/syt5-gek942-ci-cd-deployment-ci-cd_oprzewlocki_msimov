import { http } from '../http';

export interface Box {
  bId: string;           // camelCase property!
  name?: string;
  numMax?: number;
  type?: number;
  comment?: string;
  dateExported?: string; // ISO
}

export interface Page<T> { content: T[]; totalElements: number; }

export async function fetchBoxes(
  page = 0,
  size = 25,
  filters: Record<string, string> = {},
  sorts:   Record<string, string> = {}
): Promise<Page<Box>> {
  const params = new URLSearchParams();
  params.append('page', String(page));
  params.append('size', String(size));

  Object.entries(filters).forEach(([k, v]) => v?.trim() && params.append(`filter[${k}]`, v));
  Object.entries(sorts).forEach(([k, dir]) => dir && params.append(`sort[${k}]`, dir));

  const { data } = await http.get(`/box?${params.toString()}`);
  return Array.isArray(data) ? { content: data, totalElements: data.length } : data;
}

// services/tables/box.service.ts
export async function createBox(box: any) {
  const id = box.bid ?? box.bId ?? box.b_id;
  const payload = {
    ...box,
    bid: id,
    dateExported: box.dateExported ?? new Date().toISOString().slice(0, 19),
  };
  console.log('[box.create] payload', payload);
  const { data } = await http.post('/box', payload);
  return data;
}


export async function updateBox(box: any) {
  const id = box.bId ?? box.bid ?? box.b_id;
  return http.put(`/box/${id}`, box);
}

export async function deleteBox(bId: string) {
  await http.delete(`/box/${bId}`);
}

export async function fetchLatestBox() {
  const { data } = await http.get('/box', {
    params: {
      page: 0,
      size: 1,
      'sort[bId]': 'desc',
    },
  });
  return (data?.content?.[0]) ?? null;
}

export function nextBoxIdFrom(current?: string): string {
  // Fallback
  if (!current) return 'V001';
  const m = String(current).match(/^V(\d{3})$/i);
  if (!m) return 'V001';
  const n = String(parseInt(m[1], 10) + 1).padStart(3, '0');
  return `V${n}`;
}

export async function checkBoxDelete(bId: string) {
  return http.get(`/box/advanced/${bId}`, { params: { action: "check" } });
}

export async function cascadeDeleteBox(bId: string) {
    return http.delete(`/api/box/advanced/${bId}`, { params: { action: "cascade" } });
}

export function generateNextBoxId(existing: any[]): string {
  const ids = existing
    .map(b => b.bId || b.bid || b.id)
    .filter(Boolean)
    .map(id => parseInt(id.replace(/\D/g, ""), 10))
    .filter(n => !isNaN(n));

  const next = Math.max(0, ...ids) + 1;
  return `V${String(next).padStart(3, "0")}`;
}

export async function deleteBoxWithCheck(bId: string) {
  try {
    return await deleteBox(bId);
  } catch (err: any) {
    if (err.response?.status === 409) {
      throw { code: "NEEDS_CASCADE", bId };
    }
    throw err;
  }
}