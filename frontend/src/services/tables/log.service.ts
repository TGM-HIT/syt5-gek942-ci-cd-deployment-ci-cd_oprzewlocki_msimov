import { http } from '../http';

export interface Log {
  logId?: number;
  dateCreated?: string;
  level?: string;
  info?: string;
  sId?: string;
  sStamp?: string;
  aId?: number;
  dateExported?: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
}

export async function fetchLogs(
    page = 0,
    size = 25,
    filters: Record<string, string> = {},
    sorts: Record<string, string> = {}
): Promise<Page<Log>> {
  const params = new URLSearchParams();
  params.append('page', String(page));
  params.append('size', String(size));

  Object.entries(filters).forEach(([k, v]) => {
    if (v?.trim()) params.append(`filter[${k}]`, v);
  });
  Object.entries(sorts).forEach(([k, dir]) => {
    if (dir) params.append(`sort[${k}]`, dir);
  });

  const url = `/logs?${params.toString()}`;
  const { data } = await http.get(url);
  return Array.isArray(data) ? { content: data, totalElements: data.length } : data;
}

export async function createLog(log: Log) {
  const payload = {
    dateCreated: log.dateCreated || new Date().toISOString(),
    level: log.level,
    info: log.info,
    sId: log.sId || null,
    sStamp: log.sStamp || null,
    aId: log.aId || null,
    dateExported: log.dateExported || new Date().toISOString()
  };
  const { data } = await http.post(`/logs`, payload);
  return data;
}

export async function updateLog(log: Log) {
  const payload = {
    dateCreated: log.dateCreated,
    level: log.level,
    info: log.info,
    sId: log.sId || null,
    sStamp: log.sStamp || null,
    aId: log.aId || null,
    dateExported: log.dateExported
  };
  return http.put(`/logs/${log.logId}`, payload);
}

export async function deleteLog(logId: number) {
  await http.delete(`/logs/${logId}`);
}
