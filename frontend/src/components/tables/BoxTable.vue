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
    const lastId = latest?.bId;
    const nextId = nextBoxIdFrom(lastId);

    tableRef.value?.openCreate?.({
      bId: nextId,
      name: nextId,
      dateExported: new Date().toISOString().slice(0, 19),
      numMax: 40,
      type: 1,
      comment: ""
    });
  }
});

async function validateForm(form: any) {
  const id = form.bId;
  if (!/^[A-Z]\d{3}$/i.test(id))
    return { ok: false, message: "ID must be format V### (e.g., V018)" };

  if (!(form.numMax > 0 && form.numMax < 1000))
    return { ok: false, message: "numMax must be between 1 and 999" };

  if (!(form.type > 0 && form.type < 10))
    return { ok: false, message: "type must be between 1 and 9" };

  return { ok: true };
}

// Use normalized bId from GenericCrudTable
function handleDelete(id: any, row: any) {
  console.log('Delete box - ID param:', id, 'Row:', row);

  const bId = row.bId ?? id;

  console.log('Extracted bId:', bId);

  if (!bId) {
    console.error('Invalid box ID. Full row:', JSON.stringify(row, null, 2));
    throw new Error('Invalid box ID');
  }

  return deleteBoxWithCheck(bId);
}
</script>

<template>
  <v-container fluid>
    <GenericCrudTable
        ref="tableRef"
        api-path="/boxes"
        :headers="headers"
        default-sort="bId"
        :createFn="createBox"
        :updateFn="updateBox"
        :deleteFn="handleDelete"
        :getId="row => row.bId"
        :validateForm="validateForm"
    />
  </v-container>
</template>
