import { http, encodeCompositeKey } from '../http';

export interface Sample {
  s_id: string;
  s_stamp: string;
  name?: string;
  lane?: number;
  comment?: string;
  boxposString?: string;
}

export interface Page<T> { content: T[]; totalElements: number; }

export async function fetchSamples(
  page = 0,
  size = 25,
  filters: Record<string, string> = {},
  sorts:   Record<string, string> = {}
): Promise<Page<Sample>> {
  const params = new URLSearchParams();
  params.append('page', String(page));
  params.append('size', String(size));

  Object.entries(filters).forEach(([k, v]) => {
    if (v?.trim()) params.append(`filter[${k}]`, v);
  });
  Object.entries(sorts).forEach(([k, dir]) => {
    if (dir) params.append(`sort[${k}]`, dir);
  });

  const qs = params.toString();
  const url = `/samples/dto?${qs}`;
  console.log('[samples] GET', url);   // <-- LOG

  const { data } = await http.get(url);
  return Array.isArray(data) ? { content: data, totalElements: data.length } : data;
}

export async function createSample(sample: Sample) {
  const { data } = await http.post(`/samples`, sample);
  return data;
}

export async function updateSample(sample: any) {
  const id = sample.s_id;
  const stamp = sample.s_stamp;
  return http.put(`/samples/${id},${stamp}`, sample);
}


export async function deleteSample(s_id: string, s_stamp: string) {
  const key = encodeCompositeKey([s_id, s_stamp]);
  try {
    await http.delete(`/samples/${key}`);
  } catch (err: any) {
    if (err.response?.status === 409) throw err; // forward conflict
    console.error("Delete failed:", err);
    throw err;
  }
}



export async function getSampleByKey(s_id: string, s_stamp: string) {
  const key = encodeCompositeKey([s_id, s_stamp]);
  const { data } = await http.get(`/samples/dto/${key}`);
  return data as Sample;
}
