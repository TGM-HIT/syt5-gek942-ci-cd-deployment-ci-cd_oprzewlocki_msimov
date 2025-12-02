<script setup lang="ts">
import { ref } from 'vue';
import GenericCrudTable from '@/components/crud/GenericCrudTable.vue';
import { createBox, updateBox, deleteBoxWithCheck } from '@/services/tables/box.service';
import { fetchLatestBox, nextBoxIdFrom } from '@/services/tables/box.service';

const headers = [
  { key: 'bId', title: 'Box ID' },
  { key: 'name', title: 'Name' },
  { key: 'numMax', title: 'Num Max' },
  { key: 'type', title: 'Type' },
  { key: 'comment', title: 'Comment' },
  { key: 'dateExported', title: 'Date Exported' },
];

const tableRef = ref<any>(null);

defineExpose({
  async openCreate() {
    const latest = await fetchLatestBox();
    const lastId = latest?.bId ?? latest?.bid ?? latest?.b_id;
    const nextId = nextBoxIdFrom(lastId);

    tableRef.value?.openCreate?.({
      bId: nextId,
      bid: nextId,
      name: nextId,
      dateExported: new Date().toISOString().slice(0, 19),
      numMax: 40,
      type: 1,
      comment: ""
    });
  }
});

async function validateForm(form: any) {
  const id = form.bId ?? form.bid;
  if (!/^V\d{3}$/i.test(id))
    return { ok: false, message: "ID must be format V### (e.g., V018)" };

  if (!(form.numMax > 0 && form.numMax < 1000))
    return { ok: false, message: "numMax must be between 1 and 999" };

  if (!(form.type > 0 && form.type < 10))
    return { ok: false, message: "type must be between 1 and 9" };

  return { ok: true };
}
</script>

<template>
  <v-container fluid>
    <GenericCrudTable
      ref="tableRef"
      api-path="/box"
      :headers="headers"
      default-sort="bId"
      :createFn="createBox"
      :updateFn="updateBox"

      :deleteFn="deleteBoxWithCheck"

      :getId="row => row.bId"
      :validateForm="validateForm"
    />
  </v-container>
</template>
