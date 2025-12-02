import { defineStore } from 'pinia';
import AnalysisService from '@/services/AnalysisService';

export const useAnalysisStore = defineStore('analysis', {
  state: () => ({
    items: [],
    total: 0,
    loading: false,
    page: 1,
    size: 10,
    sortBy: [],
    filter: '',
  }),

  actions: {
    async fetch() {
      this.loading = true;
      try {
        const sort =
          this.sortBy.length > 0
            ? `${this.sortBy[0].key},${this.sortBy[0].order === 'desc' ? 'desc' : 'asc'}`
            : '';
        const { items, total } = await AnalysisService.getAll({
          page: this.page,
          size: this.size,
          sort,
          filter: this.filter,
        });
        this.items = items;
        this.total = total;
      } finally {
        this.loading = false;
      }
    },

    async create(item) {
      await AnalysisService.create(item);
      await this.fetch();
    },

    async update(id, item) {
      await AnalysisService.update(id, item);
      await this.fetch();
    },

    async delete(id) {
      await AnalysisService.delete(id);
      await this.fetch();
    },
  },
});
