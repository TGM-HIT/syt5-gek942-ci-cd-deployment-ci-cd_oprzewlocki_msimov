import { ref, watch } from 'vue';

export function useServerTable<T>(loader: (page:number,size:number,sort:string,search:string)=>Promise<{content:T[];totalElements:number;}>) {
  const items = ref<T[]>([]);
  const total = ref(0);
  const loading = ref(false);
  const page = ref(1);        // vuetify is 1-based
  const itemsPerPage = ref(25);
  const sortBy = ref<{key:string;order:'asc'|'desc'}[]>([]);
  const search = ref('');

  async function load() {
    loading.value = true;
    const sort = sortBy.value[0] ? `${sortBy.value[0].key},${sortBy.value[0].order}` : 's_stamp,desc';
    const res = await loader(page.value-1, itemsPerPage.value, sort, search.value);
    items.value = res.content;
    total.value = res.totalElements;
    loading.value = false;
  }

  watch([page, itemsPerPage, sortBy, search], () => load(), { immediate: true });

  return { items, total, loading, page, itemsPerPage, sortBy, search, load };
}
