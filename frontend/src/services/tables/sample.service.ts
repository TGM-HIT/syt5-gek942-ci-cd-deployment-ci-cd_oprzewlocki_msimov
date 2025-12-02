import { http, encodeCompositeKey } from '../http';

export interface Sample {
  s_id: string;
  s_stamp: string;
  name?: string;
  weightNet?: number;
  weightBru?: number;
  weightTar?: number;
  quantity?: number;
  distance?: number;
  dateCrumbled?: string;
  sFlags?: string;
  lane?: number;
  comment?: string;
  dateExported?: string;
  boxposString?: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
}

export async function fetchSamples(
    page = 0,
    size = 25,
    filters: Record<string, string> = {},
    sorts: Record<string, string> = {}
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
  const url = `/samples?${qs}`;
  console.log('[samples] GET', url);

  const { data } = await http.get(url);
  return Array.isArray(data) ? { content: data, totalElements: data.length } : data;
}

export async function createSample(sample: Sample) {
  const payload = {
    s_id: sample.s_id,
    s_stamp: sample.s_stamp || new Date().toISOString(),
    name: sample.name,
    weightNet: sample.weightNet || 0,
    weightBru: sample.weightBru || 0,
    weightTar: sample.weightTar || 0,
    quantity: sample.quantity || 0,
    distance: sample.distance || 0,
    dateCrumbled: sample.dateCrumbled || new Date().toISOString(),
    sFlags: sample.sFlags || "-----",
    lane: sample.lane || 0,
    comment: sample.comment,
    dateExported: sample.dateExported || new Date().toISOString()
  };
  const { data } = await http.post(`/samples`, payload);
  return data;
}

export async function updateSample(sample: Sample) {
  const id = sample.s_id;
  const stamp = sample.s_stamp;
  const payload = {
    s_id: sample.s_id,
    s_stamp: sample.s_stamp,
    name: sample.name,
    weightNet: sample.weightNet,
    weightBru: sample.weightBru,
    weightTar: sample.weightTar,
    quantity: sample.quantity,
    distance: sample.distance,
    dateCrumbled: sample.dateCrumbled,
    sFlags: sample.sFlags,
    lane: sample.lane,
    comment: sample.comment,
    dateExported: sample.dateExported
  };
  return http.put(`/samples/${id},${stamp}`, payload);
}

export async function deleteSample(s_id: string, s_stamp: string) {
  const key = encodeCompositeKey([s_id, s_stamp]);
  try {
    await http.delete(`/samples/${key}`);
  } catch (err: any) {
    if (err.response?.status === 409) throw err;
    console.error("Delete failed:", err);
    throw err;
  }
}

export async function getSampleByKey(s_id: string, s_stamp: string) {
  const key = encodeCompositeKey([s_id, s_stamp]);
  const { data } = await http.get(`/samples/${key}`);
  return data as Sample;
}
