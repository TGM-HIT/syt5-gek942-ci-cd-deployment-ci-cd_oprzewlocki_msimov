import { http } from '../http';

export interface Box {
  bId: string;
  bid?: string;
  b_id?: string;
  name?: string;
  numMax?: number;
  type?: number;
  comment?: string;
  dateExported?: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
}

export async function fetchBoxes(
    page = 0,
    size = 25,
    filters: Record<string, string> = {},
    sorts: Record<string, string> = {}
): Promise<Page<Box>> {
  const params = new URLSearchParams();
  params.append('page', String(page));
  params.append('size', String(size));

  Object.entries(filters).forEach(([k, v]) => {
    if (v?.trim()) params.append(`filter[${k}]`, v);
  });
  Object.entries(sorts).forEach(([k, dir]) => {
    if (dir) params.append(`sort[${k}]`, dir);
  });

  const url = `/boxes?${params.toString()}`;
  const { data } = await http.get(url);
  return Array.isArray(data) ? { content: data, totalElements: data.length } : data;
}

export async function fetchLatestBox(): Promise<Box | null> {
  try {
    const params = new URLSearchParams();
    params.append('page', '0');
    params.append('size', '1');
    params.append('sort[bId]', 'desc');

    const { data } = await http.get(`/boxes?${params.toString()}`);

    if (Array.isArray(data)) {
      return data.length > 0 ? data[0] : null;
    }

    if (data.content && data.content.length > 0) {
      return data.content[0];
    }

    return null;
  } catch (err) {
    console.error('fetchLatestBox failed:', err);
    return null;
  }
}

export function nextBoxIdFrom(lastId: string | null | undefined): string {
  if (!lastId) {
    return 'B001';
  }

  const match = lastId.match(/^([A-Za-z]*)(\d+)$/);
  if (!match) {
    return 'B001';
  }

  const prefix = match[1] || 'B';
  const numStr = match[2];
  const num = parseInt(numStr, 10);
  const nextNum = num + 1;

  const paddedNum = String(nextNum).padStart(numStr.length, '0');
  return `${prefix}${paddedNum}`;
}

export async function createBox(box: Box) {
  const payload = {
    bId: box.bId,      // Try camelCase first
    bid: box.bId,      // Fallback to lowercase
    b_id: box.bId,     // Fallback to snake_case
    name: box.name,
    numMax: box.numMax,
    num_max: box.numMax,  // Backend might expect snake_case
    type: box.type,
    comment: box.comment,
    dateExported: box.dateExported || new Date().toISOString(),
    date_exported: box.dateExported || new Date().toISOString()
  };
  const { data } = await http.post(`/boxes`, payload);
  return data;
}

export async function updateBox(box: Box) {
  const payload = {
    bId: box.bId,      // Try camelCase first
    bid: box.bId,      // Fallback to lowercase
    b_id: box.bId,     // Fallback to snake_case
    name: box.name,
    numMax: box.numMax,
    num_max: box.numMax,  // Backend might expect snake_case
    type: box.type,
    comment: box.comment,
    dateExported: box.dateExported || new Date().toISOString(),
    date_exported: box.dateExported || new Date().toISOString()
  };
  return http.put(`/boxes/${box.bId}`, payload);
}

export async function deleteBox(bId: string) {
  await http.delete(`/boxes/${bId}`);
}

export async function deleteBoxWithCheck(bId: string) {
  try {
    await http.delete(`/boxes/${bId}`);
    return { success: true };
  } catch (err: any) {
    if (err.response?.status === 409) {
      const errorData = err.response.data;
      return {
        success: false,
        conflict: true,
        message: errorData?.message || 'Box has related data',
        detail: errorData?.detail || ''
      };
    }
    throw err;
  }
}
