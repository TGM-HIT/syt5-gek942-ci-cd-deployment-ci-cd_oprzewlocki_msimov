import { http } from '../http';

export interface Analysis {
  aId?: number;
  sId: string;
  sStamp: string;
  pol?: number;
  nat?: number;
  kal?: number;
  an?: number;
  glu?: number;
  dry?: number;
  lane?: number;
  comment?: string;
  dateIn?: string;
  dateOut?: string;
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

  const { data } = await http.get(`/analysis/dto?${params.toString()}`);

  // Normalisiere auf Page-Objekt
  const pageData = Array.isArray(data)
    ? { content: data, totalElements: data.length }
    : data;

    pageData.content = pageData.content.map((a: any) => ({
    ...a,
    sId: a.sample?.id?.sId,
    sStamp: a.sample?.id?.sStamp,
    }));


  return pageData;
}



export async function createAnalysis(a: Analysis) {
  const payload = {
    ...a,
    sample: {
      s_id: a.sId,
      s_stamp: a.sStamp,
    },
  };
  const { data } = await http.post(`/analysis`, payload);
  return data;
}

export async function updateAnalysis(a: Analysis) {
  const payload = {
    ...a,
    sample: {
      s_id: a.sId,
      s_stamp: a.sStamp,
    },
  };
  return http.put(`/analysis/${a.aId}`, payload);
}


export async function deleteAnalysis(aId: number) {
  await http.delete(`/analysis/${aId}`);
}

export async function validateSample(sId: string, sStamp: string) {
  const { data } = await http.get(`/analysis/validate-sample`, {
    params: { sId, sStamp },
  });
  return data.valid as boolean;
}

export async function validateSampleRef(sId: string, sStamp: string) {
  const { data } = await http.get('/analysis/validate-sample', {
    params: { sId, sStamp },
  });
  return !!data.valid;
}

