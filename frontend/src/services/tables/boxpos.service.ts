import { http, encodeCompositeKey } from "../http";

export interface BoxPos {
  bposId: number;
  bId: string;
  sId: string;
  sStamp: string;
  dateExported?: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
}

export async function fetchBoxPos(page=0, size=25, filters={}, sorts={}): Promise<Page<BoxPos>> {
  const params = new URLSearchParams();
  params.append("page", String(page));
  params.append("size", String(size));

  Object.entries(filters).forEach(([k,v]) => v && params.append(`filter[${k}]`, v));
  Object.entries(sorts).forEach(([k,v]) => v && params.append(`sort[${k}]`, v));

  const { data } = await http.get(`/boxpos?${params.toString()}`);
  const pageData = Array.isArray(data) ? { content: data, totalElements: data.length } : data;

  pageData.content = pageData.content.map((bp: any) => ({
    bposId: bp.id?.bposId ?? bp.bposId,
    bId: bp.id?.bId ?? bp.id?.bid,
    sId: bp.sId,
    sStamp: bp.sStamp,
    dateExported: bp.dateExported
  }));

  return pageData;
}

export async function createBoxPos(data: any) {
  return http.post(`/boxpos/flat`, {
    bposId: Number(data.bposId),
    bId: data.bId,
    sId: data.sId,
    sStamp: data.sStamp,
    dateExported: data.dateExported ?? null
  });
}

export async function updateBoxPos(data: any) {
  return http.put(`/boxpos/flat/${data.bposId},${data.bId}`, {
    bposId: Number(data.bposId),
    bId: data.bId,
    sId: data.sId,
    sStamp: data.sStamp,
    dateExported: data.dateExported ?? null
  });
}


export async function deleteBoxPos(bposId: number, bId: string) {
  const key = encodeCompositeKey([bposId, bId]);
  return http.delete(`/boxpos/${key}`);
}
