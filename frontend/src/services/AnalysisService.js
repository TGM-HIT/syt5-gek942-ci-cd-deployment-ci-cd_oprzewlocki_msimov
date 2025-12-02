import api from '@/api/axios';

export default {
  async getAll({ page = 1, size = 10, sort = '', filter = '' } = {}) {
    const params = {
        page: page - 1, // Spring = 0-based
        size,
        sort,
        filterText: filter,
    };

    const { data } = await api.get('/analysis', { params });

    const items = data.content.map((item) => ({
        id: item.aid,
        sampleId: item.sample?.s_id ?? '-',
        sampleStamp: item.sample?.s_stamp ?? '-',
        created: item.dateExported ?? '-',
        valueA: item.pol ?? '-',
        valueB: item.nat ?? '-',
        status: item.comment ?? '-',
    }));

    return { items, total: data.totalElements };
    },


  async getById(id) {
    const { data } = await api.get(`/analysis/${id}`);
    return data;
  },

    async create(payload) {
    const now = new Date().toISOString();

    const backendPayload = {
        sample: {
        s_id: payload.sampleId ?? "0000000000000",
        s_stamp: now,
        name: payload.sampleName ?? null,
        weightNet: payload.weightNet ?? 0,
        weightBru: payload.weightBru ?? 0,
        weightTar: payload.weightTar ?? 0,
        quantity: payload.quantity ?? 0,
        distance: payload.distance ?? 0,
        dateCrumbled: now,
        getsFlags: "-----",
        lane: payload.lane ?? 0,
        comment: payload.comment ?? null,
        dateExported: now
        },
        pol: payload.valueA ?? 0,
        nat: payload.valueB ?? 0,
        kal: payload.kal ?? 0,
        an: payload.an ?? 0,
        glu: payload.glu ?? 0,
        dry: payload.dry ?? 0,
        dateIn: now,
        dateOut: now,
        weightMea: payload.weightMea ?? 0,
        weightNrm: payload.weightNrm ?? 0,
        weightCur: payload.weightCur ?? 0,
        weightDif: payload.weightDif ?? 0,
        density: payload.density ?? 0,
        lane: payload.lane ?? 0,
        comment: payload.status ?? "Normal",
        dateExported: now,
        aid: payload.id ?? 0,
        aflags: "-----------"
    };

    const { data } = await api.post('/analysis', backendPayload);
    return data;
    },


    async update(id, payload) {
        const body = {
            sample: {
            s_id: payload.sampleId,
            s_stamp: payload.sampleStamp, // from original analysis.sample.s_stamp
            name: payload.sampleName || "string",
            weightNet: payload.sampleWeightNet || 0,
            weightBru: payload.sampleWeightBru || 0,
            weightTar: payload.sampleWeightTar || 0,
            quantity: payload.sampleQuantity || 0,
            distance: payload.sampleDistance || 0,
            dateCrumbled: payload.sampleDateCrumbled || payload.sampleStamp,
            getsFlags: payload.sampleFlags || "-----",
            lane: payload.sampleLane || 0,
            comment: payload.status || "Normal",
            dateExported: payload.sampleDateExported || payload.sampleStamp
            },
            pol: payload.valueA,
            nat: payload.valueB,
            kal: payload.kal || 0,
            an: payload.an || 0,
            glu: payload.glu || 0,
            dry: payload.dry || 0,
            dateIn: payload.dateIn, // from DB (don’t replace!)
            dateOut: payload.dateOut, // from DB (don’t replace!)
            weightMea: payload.weightMea || 0,
            weightNrm: payload.weightNrm || 0,
            weightCur: payload.weightCur || 0,
            weightDif: payload.weightDif || 0,
            density: payload.density || 0,
            lane: payload.lane || 0,
            comment: payload.status || "Normal",
            dateExported: payload.dateExported, // from DB
            aid: id,
            aflags: payload.aflags || "M----------"
        };

        console.log("Final PUT request body:", JSON.stringify(body, null, 2));
        const { data } = await api.put(`/analysis/${id}`, body);
        return data;
        },



  async delete(id) {
    await api.delete(`/analysis/${id}`);
  }
};
