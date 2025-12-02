import { http } from '../http';

export interface Analysis {
  aId?: number;
  sId?: string;
  sStamp?: string;
  pol?: number;
  nat?: number;
  kal?: number;
  an?: number;
  glu?: number;
  dry?: number;
  lane?: number;
  comment?: string;
  aFlags?: string;
  dateIn?: string;
  dateOut?: string;
  weightMea?: number;
  weightNrm?: number;
  weightCur?: number;
  weightDif?: number;
  density?: number;
  dateExported?: string;
  boxposString?: string;
}

export async function fetchAnalysis(
    page = 0,
    size = 25,
    filters: Record<string, string> = {},
    sorts: Record<string, string> = {}
) {
  const params = new URLSearchParams();
  params.append("page", String(page));
  params.append("size", String(size));

  Object.entries(filters).forEach(([k, v]) => v && params.append(`filter[${k}]`, v));
  Object.entries(sorts).forEach(([k, v]) => v && params.append(`sort[${k}]`, v));

  const { data } = await http.get(`/analysis?${params.toString()}`);

  const pageData = Array.isArray(data)
      ? { content: data, totalElements: data.length }
      : data;

  return pageData;
}

export async function createAnalysis(a: Analysis) {
  const payload = {
    sId: a.sId || null,
    sStamp: a.sStamp || null,
    pol: a.pol,
    nat: a.nat,
    kal: a.kal,
    an: a.an,
    glu: a.glu,
    dry: a.dry,
    dateIn: a.dateIn || new Date().toISOString(),
    dateOut: a.dateOut || new Date().toISOString(),
    weightMea: a.weightMea,
    weightNrm: a.weightNrm,
    weightCur: a.weightCur,
    weightDif: a.weightDif,
    density: a.density,
    lane: a.lane,
    comment: a.comment,
    aFlags: a.aFlags || "-",
    dateExported: a.dateExported || new Date().toISOString()
  };
  const { data } = await http.post(`/analysis`, payload);
  return data;
}

export async function updateAnalysis(a: Analysis) {
  const payload = {
    sId: a.sId || null,
    sStamp: a.sStamp || null,
    pol: a.pol,
    nat: a.nat,
    kal: a.kal,
    an: a.an,
    glu: a.glu,
    dry: a.dry,
    dateIn: a.dateIn,
    dateOut: a.dateOut,
    weightMea: a.weightMea,
    weightNrm: a.weightNrm,
    weightCur: a.weightCur,
    weightDif: a.weightDif,
    density: a.density,
    lane: a.lane,
    comment: a.comment,
    aFlags: a.aFlags,
    dateExported: a.dateExported
  };
  return http.put(`/analysis/${a.aId}`, payload);
}

export async function deleteAnalysis(aId: number) {
  await http.delete(`/analysis/${aId}`);
}

export async function validateSample(sId: string, sStamp: string) {
  const { data } = await http.get(`/analysis/validate-sample`, {
    params: { sId: sId, sStamp: sStamp },
  });
  return data.valid as boolean;
}

export async function validateSampleRef(sId: string, sStamp: string) {
  const { data } = await http.get('/analysis/validate-sample', {
    params: { sId: sId, sStamp: sStamp },
  });
  return !!data.valid;
}
