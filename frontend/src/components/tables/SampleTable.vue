<script setup lang="ts">
import { ref } from 'vue';
import GenericCrudTable from '@/components/crud/GenericCrudTable.vue';
import { createSample, updateSample, deleteSample } from '@/services/tables/sample.service';

const headers = [
  { key: 's_id', title: 'ID' },
  { key: 's_stamp', title: 'Timestamp' },
  { key: 'boxposString', title: 'BoxPos' },
  { key: 'name', title: 'Name' },
  { key: 'lane', title: 'Lane' },
  { key: 'comment', title: 'Comment' },
];

const tableRef = ref<any>(null);
defineExpose({ openCreate: () => tableRef.value?.openCreate?.() });
</script>

<template>
  <v-container fluid>
    <GenericCrudTable
      ref="tableRef"
      api-path="/samples/dto"
      :headers="headers"
      default-sort="s_stamp"
      :createFn="createSample"
      :updateFn="updateSample"
      :deleteFn="(_id: any, row: { s_id: string; s_stamp: string }) => deleteSample(row.s_id, row.s_stamp)"
      :getId="(row: { s_id: string; s_stamp: string }) => `${row.s_id},${row.s_stamp}`"
    />
  </v-container>
</template>
