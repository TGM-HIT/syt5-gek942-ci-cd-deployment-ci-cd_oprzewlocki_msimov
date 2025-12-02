<script setup lang="ts">
import { ref } from "vue";
import GenericCrudTable from "@/components/crud/GenericCrudTable.vue";
import { createThreshold, updateThreshold, deleteThreshold } from "@/services/tables/threshold.service";

const headers = [
  { key: "thId", title: "Threshold ID" },
  { key: "valueMin", title: "Min Value" },
  { key: "valueMax", title: "Max Value" },
  { key: "dateChanged", title: "Last Changed" },
];

const tableRef = ref<any>(null);
defineExpose({ openCreate: () => tableRef.value?.openCreate?.({
  dateChanged: new Date().toISOString().slice(0, 19),
}) });

// Optional validation
async function validateForm(form: any) {
  if (!form.thId) return { ok:false, message:"thId required" };
  if (form.valueMin != null && form.valueMax != null && Number(form.valueMin) > Number(form.valueMax))
    return { ok:false, message:"valueMin cannot be greater than valueMax" };

  return { ok:true };
}
</script>

<template>
  <v-container fluid>
    <GenericCrudTable
      ref="tableRef"
      api-path="/thresholds"
      :headers="headers"
      default-sort="thId"
      :createFn="createThreshold"
      :updateFn="updateThreshold"
      :deleteFn="(id) => deleteThreshold(id)"
      :getId="row => row.thId"
      :validateForm="validateForm"
    />
  </v-container>
</template>
