<script setup lang="ts">
import { ref } from 'vue';
import GenericCrudTable from '@/components/crud/GenericCrudTable.vue';
import { createLog, updateLog, deleteLog } from '@/services/tables/log.service';

const headers = [
  { key: 'logId', title: 'ID' },
  { key: 'dateCreated', title: 'Created' },
  { key: 'level', title: 'Level' },
  { key: 'info', title: 'Info' },
  { key: 'sId', title: 'Sample ID' },
  { key: 'sStamp', title: 'Sample Timestamp' },
  { key: 'aId', title: 'Analysis ID' },
  { key: 'dateExported', title: 'Exported' },
];

const tableRef = ref<any>(null);
defineExpose({ openCreate: () => tableRef.value?.openCreate?.({
  dateCreated: new Date().toISOString().slice(0,19)
}) });

async function validateForm(form:any) {
  if(!form.level) return {ok:false, message:"level required"};
  if(!form.sId || !form.sStamp) return {ok:false, message:"Sample reference required"};
  return {ok:true};
}
</script>

<template>
  <v-container fluid>
    <GenericCrudTable
      ref="tableRef"
      api-path="/logs"
      :headers="headers"
      default-sort="logId"
      :createFn="createLog"
      :updateFn="updateLog"
      :deleteFn="id => deleteLog(id)"
      :getId="row => row.logId"
      :validateForm="validateForm"
    />
  </v-container>
</template>
