<script setup lang="ts">
import { ref } from 'vue';
import GenericCrudTable from '@/components/crud/GenericCrudTable.vue';
import { createSample, updateSample, deleteSample, fetchSamples } from '@/services/tables/sample.service';

const headers = [
  { key: 's_id', title: 'Sample ID' },
  { key: 's_stamp', title: 'Timestamp' },
  { key: 'name', title: 'Name' },
  { key: 'weightNet', title: 'Weight Net' },
  { key: 'lane', title: 'Lane' },
  { key: 'boxposString', title: 'Box Position' },
  { key: 'comment', title: 'Comment' },
];

const tableRef = ref<any>(null);

// Generate sample ID: YYMMDDHHMMSSN (13 digits)
async function generateSampleId(): Promise<string> {
  const now = new Date();

  const yy = String(now.getFullYear()).slice(-2);
  const mm = String(now.getMonth() + 1).padStart(2, '0');
  const dd = String(now.getDate()).padStart(2, '0');
  const hh = String(now.getHours()).padStart(2, '0');
  const min = String(now.getMinutes()).padStart(2, '0');
  const ss = String(now.getSeconds()).padStart(2, '0');

  // Base: YYMMDDHHMM SS (12 digits)
  const base = `${yy}${mm}${dd}${hh}${min}${ss}`;

  // Fetch latest sample to determine incrementing digit
  try {
    const result = await fetchSamples(0, 1, {}, { s_id: 'desc' });

    if (result.content && result.content.length > 0) {
      const latestId = result.content[0].s_id ?? result.content[0].sId;

      if (latestId && latestId.startsWith(base)) {
        // Same second, increment last digit
        const lastDigit = parseInt(latestId.charAt(12), 10);
        const nextDigit = (lastDigit + 1) % 10;
        return base + nextDigit;
      }
    }
  } catch (err) {
    console.warn('Could not fetch latest sample:', err);
  }

  // Different second or first sample, use digit 0
  return base + '0';
}

defineExpose({
  async openCreate() {
    const sId = await generateSampleId();
    const sStamp = new Date().toISOString().slice(0, 19);

    tableRef.value?.openCreate?.({
      s_id: sId,
      s_stamp: sStamp,
      weightNet: 0,
      weightBru: 0,
      weightTar: 0,
      quantity: 0,
      lane: 1,
      distance: 0,
      dateCrumbled: sStamp,
      sFlags: '-----',
      comment: ''
    });
  }
});

function handleDelete(id: string, row: any) {
  const sId = row.sId ?? row.s_id;
  const sStamp = row.sStamp ?? row.s_stamp;

  if (!sId || !sStamp) {
    console.error('Missing sample key:', row);
    throw new Error('Invalid sample key');
  }

  return deleteSample(sId, sStamp);
}
</script>

<template>
  <v-container fluid>
    <GenericCrudTable
        ref="tableRef"
        api-path="/samples"
        :headers="headers"
        default-sort="s_stamp"
        :createFn="createSample"
        :updateFn="updateSample"
        :deleteFn="handleDelete"
        :getId="row => `${row.sId},${row.sStamp}`"
    />
  </v-container>
</template>
