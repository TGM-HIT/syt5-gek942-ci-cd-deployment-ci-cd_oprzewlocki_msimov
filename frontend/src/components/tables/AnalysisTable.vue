<script setup lang="ts">
import GenericCrudTable from '@/components/crud/GenericCrudTable.vue';
import { createAnalysis, updateAnalysis, deleteAnalysis, validateSampleRef } from '@/services/tables/analysis.service';
import { ref } from 'vue';

const headers = [
  { key: 'aId', title: 'Analysis ID' },
  { key: 'sId', title: 'Sample ID' },
  { key: 'sStamp', title: 'Sample Timestamp' },
  { key: 'boxposString', title: 'BoxPos' },
  { key: 'pol', title: 'Pol' },
  { key: 'nat', title: 'Nat' },
  { key: 'kal', title: 'Kal' },
  { key: 'an', title: 'An' },
  { key: 'glu', title: 'Glu' },
  { key: 'dry', title: 'Dry' },
  { key: 'lane', title: 'Lane' },
  { key: 'comment', title: 'Comment' },
  { key: 'dateIn', title: 'Date In' },
  { key: 'dateOut', title: 'Date Out' },
];


const tableRef = ref<any>(null);
defineExpose({ openCreate: () => tableRef.value?.openCreate?.() });


const validateForm = async (form: any) => {
  if (!form.s_id || !form.s_stamp)
    return { ok: false, message: 's_id and s_stamp are required' };

  const ok = await validateSampleRef(form.s_id, form.s_stamp);
  return ok ? { ok: true } : { ok: false, message: 'Invalid Sample reference.' };
};
</script>

<template>
  <v-container fluid>
    <GenericCrudTable
        ref="tableRef"
      api-path="/analysis/dto"
      :headers="headers"
      default-sort="aId"
      :createFn="createAnalysis"
      :updateFn="updateAnalysis"
      :deleteFn="(id) => deleteAnalysis(id as number)"
      :validateForm="validateForm"
      :getId="row => row.aId"
    />
  </v-container>
</template>
