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

async function generateSampleId(): Promise<string> {
  const now = new Date();

  const yy = String(now.getFullYear()).slice(-2);
  const mm = String(now.getMonth() + 1).padStart(2, '0');
  const dd = String(now.getDate()).padStart(2, '0');
  const hh = String(now.getHours()).padStart(2, '0');
  const min = String(now.getMinutes()).padStart(2, '0');
  const ss = String(now.getSeconds()).padStart(2, '0');

  const base = `${yy}${mm}${dd}${hh}${min}${ss}`;

  try {
    const result = await fetchSamples(0, 1, {}, { s_id: 'desc' });

    if (result.content && result.content.length > 0) {
      const latestId = result.content[0].s_id ?? result.content[0].sId;

      if (latestId && latestId.startsWith(base)) {
        const lastDigit = parseInt(latestId.charAt(12), 10);
        const nextDigit = (lastDigit + 1) % 10;
        return base + nextDigit;
      }
    }
  } catch (err) {
    console.warn('Could not fetch latest sample:', err);
  }

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

// MINIMAL validation - only for fields shown in form
async function validateForm(form: any) {
  // s_id and s_stamp are auto-generated, readonly

  if (!form.name || form.name.trim().length === 0) {
    return {
      ok: false,
      message: 'Name is required'
    };
  }

  if (form.weightNet < 0) {
    return {
      ok: false,
      message: 'Weight Net cannot be negative'
    };
  }

  if (form.lane < 1 || form.lane > 20) {
    return {
      ok: false,
      message: 'Lane must be between 1 and 20'
    };
  }

  return { ok: true };
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
        :validateForm="validateForm"
    />
  </v-container>
</template>
