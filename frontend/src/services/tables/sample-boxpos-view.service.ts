import { http } from '../http';

export interface SampleBoxPosView {
    sid: string;
    sstamp: string;
    boxpos: string;
}

export interface Page<T> {
    content: T[];
    totalElements: number;
}

export async function fetchSampleBoxPosView(
    page = 0,
    size = 25,
    filters: Record<string, string> = {},
    sorts: Record<string, string> = {}
): Promise<Page<SampleBoxPosView>> {
    const params = new URLSearchParams();
    params.append('page', String(page));
    params.append('size', String(size));

    Object.entries(filters).forEach(([k, v]) => {
        if (v?.trim()) params.append(`filter[${k}]`, v);
    });
    Object.entries(sorts).forEach(([k, dir]) => {
        if (dir) params.append(`sort[${k}]`, dir);
    });

    const url = `/sample-boxpos?${params.toString()}`;
    const { data } = await http.get(url);
    return Array.isArray(data) ? { content: data, totalElements: data.length } : data;
}
