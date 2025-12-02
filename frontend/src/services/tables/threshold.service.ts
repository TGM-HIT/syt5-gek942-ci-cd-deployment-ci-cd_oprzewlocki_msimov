import { http } from "../http";

export interface Threshold {
  thId: string;
  valueMin?: number;
  valueMax?: number;
  dateChanged?: string;
}

export interface Page<T> { content: T[]; totalElements: number; }

export async function fetchThresholds(
  page = 0,
  size = 25,
  filters: Record<string, string> = {},
  sorts:   Record<string, string> = {}
): Promise<Page<Threshold>> {
  const params = new URLSearchParams();
  params.append("page", String(page));
  params.append("size", String(size));

  Object.entries(filters).forEach(([k, v]) => v?.trim() && params.append(`filter[${k}]`, v));
  Object.entries(sorts).forEach(([k, dir]) => dir && params.append(`sort[${k}]`, dir));

  const { data } = await http.get(`/thresholds?${params.toString()}`);
  return Array.isArray(data) ? { content: data, totalElements: data.length } : data;
}

export async function createThreshold(th: Threshold) {
  const payload = {
    ...th,
    dateChanged: th.dateChanged ?? new Date().toISOString().slice(0, 19),
  };
  const { data } = await http.post(`/thresholds`, payload);
  return data;
}

export async function updateThreshold(th: Threshold) {
  return http.put(`/thresholds/${th.thId}`, th);
}

export async function deleteThreshold(thId: string) {
  return http.delete(`/thresholds/${thId}`);
}
