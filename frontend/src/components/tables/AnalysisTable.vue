<script setup lang="ts">
import { ref } from 'vue';
import GenericCrudTable from '@/components/crud/GenericCrudTable.vue';
import { createAnalysis, updateAnalysis, deleteAnalysis } from '@/services/tables/analysis.service';

const headers = [
  { key: 'aId', title: 'Analysis ID' },
  { key: 'sId', title: 'Sample ID' },
  { key: 'sStamp', title: 'Sample Timestamp' },
  { key: 'lane', title: 'Lane' },
  { key: 'pol', title: 'POL' },
  { key: 'nat', title: 'NAT' },
  { key: 'kal', title: 'KAL' },
  { key: 'an', title: 'AN' },
  { key: 'glu', title: 'GLU' },
  { key: 'dry', title: 'DRY' },
  { key: 'dateIn', title: 'Date In' },
  { key: 'dateOut', title: 'Date Out' },
  { key: 'boxposString', title: 'Box Position' },
  { key: 'comment', title: 'Comment' },
];

const tableRef = ref<any>(null);

defineExpose({
  openCreate: () => tableRef.value?.openCreate?.({
    aId: '',
    sId: '',
    s_stamp: new Date().toISOString().slice(0, 19),
    lane: 1,
    pol: 0,
    nat: 0,
    kal: 0,
    an: 0,
    glu: 0,
    dry: 0,
    dateIn: new Date().toISOString().slice(0, 19),
    dateOut: new Date().toISOString().slice(0, 19),
    comment: ''
  })
});

// MINIMAL validation - only for fields shown in form
async function validateForm(form: any) {
  // sId and sStamp are user input (reference to existing sample)
  if (!form.sId || form.sId.trim().length === 0) {
    return {
      ok: false,
      message: 'Sample ID (sId) is required'
    };
  }

  if (!form.sStamp) {
    return {
      ok: false,
      message: 'Sample Timestamp (sStamp) is required'
    };
  }

  // boxposString is readonly/auto

  // Validate pol (0-100)
  if (form.pol !== null && form.pol !== undefined && form.pol !== '') {
    const polVal = parseFloat(form.pol);
    if (isNaN(polVal) || polVal < 0 || polVal > 100) {
      return {
        ok: false,
        message: 'POL must be between 0 and 100'
      };
    }
  }

  // Validate nat (0-100)
  if (form.nat !== null && form.nat !== undefined && form.nat !== '') {
    const natVal = parseFloat(form.nat);
    if (isNaN(natVal) || natVal < 0 || natVal > 100) {
      return {
        ok: false,
        message: 'NAT must be between 0 and 100'
      };
    }
  }

  return { ok: true };
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
        :deleteFn="deleteAnalysis"
        :getId="row => row.aId"
        :validateForm="validateForm"
    />
  </v-container>
</template>
