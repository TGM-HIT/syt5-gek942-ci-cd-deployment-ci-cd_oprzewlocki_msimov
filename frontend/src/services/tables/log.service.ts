import { http } from '../http';

export interface LogEntry {
  logId?: number;
  dateCreated?: string;
  level?: string;
  info?: string;
  sId?: string;
  sStamp?: string;
  aId?: number;
  dateExported?: string;
}

const base = "/logs"; // <--- KEY FIX (no /api here)

export async function fetchLogs(page=0,size=25,filters={},sorts={}) {
  const params = new URLSearchParams();
  params.append("page", String(page));
  params.append("size", String(size));

  Object.entries(filters).forEach(([k,v])=>v && params.append(`filter[${k}]`, v));
  Object.entries(sorts).forEach(([k,v])=>v && params.append(`sort[${k}]`, v));

  const { data } = await http.get(base, { params });

  const pageData = Array.isArray(data)
    ? { content:data, totalElements:data.length }
    : data;

  pageData.content = pageData.content.map((l: any) => ({
    ...l,
    logId: l.logId ?? l.log_id,
    dateCreated: l.dateCreated ?? l.date_created,
    sId: l.sId ?? l.sample?.id?.sId ?? l.s_id,
    sStamp: l.sStamp ?? l.sample?.id?.sStamp ?? l.s_stamp,
    aId: l.aId ?? l.analysis?.aId ?? l.a_id,
    dateExported: l.dateExported ?? l.date_exported,
  }));

  return pageData;
}

export async function createLog(l: LogEntry) {
  const payload = {
    ...l,
    dateCreated: l.dateCreated ?? new Date().toISOString().slice(0,19),
    s_id: l.sId,
    s_stamp: l.sStamp,
    a_id: l.aId,
  };
  const { data } = await http.post(base, payload);
  return data;
}

export async function updateLog(l: LogEntry) {
  return http.put(`${base}/${l.logId}`, {
    ...l,
    s_id: l.sId,
    s_stamp: l.sStamp,
    a_id: l.aId,
  });
}

export async function deleteLog(id: number) {
  return http.delete(`${base}/${id}`);
}
