<script setup lang="ts">
import GenericCrudTable from "@/components/crud/GenericCrudTable.vue";
import { createBoxPos, updateBoxPos, deleteBoxPos } from "@/services/tables/boxpos.service";
import { ref } from "vue";

const headers = [
  { key: "bposId", title: "Position" },
  { key: "bId", title: "Box ID" },
  { key: "sId", title: "Sample ID" },
  { key: "sStamp", title: "Sample Timestamp" },
  { key: "dateExported", title: "Date Exported" },
];

const tableRef = ref<any>(null);
defineExpose({ openCreate: () => tableRef.value?.openCreate?.() });

</script>

<template>
  <v-container fluid>
    <GenericCrudTable
      ref="tableRef"
      apiPath="/boxpos"
      :headers="headers"
      default-sort="bposId"
      :createFn="createBoxPos"
      :updateFn="updateBoxPos"
      :deleteFn="(id, row) => deleteBoxPos(row.bposId, row.bId)"
      :getId="row => `${row.bposId},${row.bId}`"
    />
  </v-container>
</template>
