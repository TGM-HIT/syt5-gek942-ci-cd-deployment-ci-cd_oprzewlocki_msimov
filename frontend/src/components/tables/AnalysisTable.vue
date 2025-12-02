<script setup lang="ts">
import { ref } from 'vue';
import GenericCrudTable from '@/components/crud/GenericCrudTable.vue';
import { createAnalysis, updateAnalysis, deleteAnalysis } from '@/services/tables/analysis.service';

const headers = [
  { key: 'aId', title: 'ID' },
  { key: 'sId', title: 'Sample ID' },
  { key: 'sStamp', title: 'Sample Stamp' },
  { key: 'boxposString', title: 'Box Position' },
  { key: 'pol', title: 'POL' },
  { key: 'nat', title: 'NAT' },
  { key: 'comment', title: 'Comment' },
];

const tableRef = ref<any>(null);
defineExpose({
  openCreate: () => tableRef.value?.openCreate?.({
    sId: null,
    sStamp: null,
    pol: 0,
    nat: 0,
    kal: 0,
    an: 0,
    glu: 0,
    dry: 0,
    lane: 0,
    dateIn: new Date().toISOString(),
    dateOut: new Date().toISOString()
  })
});

// Accept both ID string and row object
function handleDelete(id: any, row: any) {
  console.log('Delete analysis - ID param:', id, 'Row:', row);

  // Try to extract aId from multiple sources
  const aId = row.aId ?? row.aid ?? row.a_id ?? id;

  console.log('Extracted aId:', aId, 'type:', typeof aId);

  if (!aId || aId === 'undefined') {
    console.error('Invalid analysis ID. Full row:', JSON.stringify(row, null, 2));
    throw new Error('Invalid analysis ID');
  }

  const numericId = typeof aId === 'number' ? aId : parseInt(String(aId), 10);

  if (isNaN(numericId)) {
    console.error('aId is not a number:', aId);
    throw new Error('Analysis ID must be numeric');
  }

  console.log('Deleting analysis with ID:', numericId);
  return deleteAnalysis(numericId);
}
</script>

<template>
  <v-container fluid>
    <GenericCrudTable
        ref="tableRef"
        api-path="/analysis"
        :headers="headers"
        default-sort="aId"
        :createFn="createAnalysis"
        :updateFn="updateAnalysis"
        :deleteFn="handleDelete"
        :getId="row => row.aId ?? row.aid ?? row.a_id"
    />
  </v-container>
</template>
