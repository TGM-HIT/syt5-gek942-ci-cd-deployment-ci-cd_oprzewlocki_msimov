import { http, encodeCompositeKey } from '../http';

export interface BoxPos {
  bposId: number;
  bId: string;
  sId: string;
  sStamp: string;
  dateExported?: string;
}

export interface Page<T> { content: T[]; totalElements: number; }

export async function fetchBoxPos(
    page = 0,
    size = 25,
    filters: Record<string, string> = {},
    sorts: Record<string, string> = {}
): Promise<Page<BoxPos>> {
  const params = new URLSearchParams();
  params.append('page', String(page));
  params.append('size', String(size));

  Object.entries(filters).forEach(([k, v]) => {
    if (v?.trim()) params.append(`filter[${k}]`, v);
  });
  Object.entries(sorts).forEach(([k, dir]) => {
    if (dir) params.append(`sort[${k}]`, dir);
  });

  const url = `/boxpos?${params.toString()}`;
  const { data } = await http.get(url);
  return Array.isArray(data) ? { content: data, totalElements: data.length } : data;
}

export async function createBoxPos(bp: BoxPos) {
  const payload = {
    bposId: bp.bposId,
    bId: bp.bId,
    sId: bp.sId,
    sStamp: bp.sStamp,
    dateExported: bp.dateExported || new Date().toISOString()
  };
  const { data } = await http.post(`/boxpos`, payload);
  return data;
}

export async function updateBoxPos(bp: BoxPos) {
  const key = encodeCompositeKey([bp.bposId, bp.bId]);
  const payload = {
    bposId: bp.bposId,
    bId: bp.bId,
    sId: bp.sId,
    sStamp: bp.sStamp,
    dateExported: bp.dateExported
  };
  return http.put(`/boxpos/${key}`, payload);
}

export async function deleteBoxPos(bposId: number, bId: string) {
  const key = encodeCompositeKey([bposId, bId]);
  await http.delete(`/boxpos/${key}`);
}
