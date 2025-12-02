<script setup lang="ts">
import { ref } from 'vue';
import GenericCrudTable from '@/components/crud/GenericCrudTable.vue';

const headers = [
  { key: 'sid', title: 'Sample ID' },
  { key: 'sstamp', title: 'Timestamp' },
  { key: 'boxpos', title: 'Box Positions' },
];

const tableRef = ref<any>(null);

// Read-only view - no create/update/delete
const noOp = () => Promise.reject('This view is read-only');
</script>

<template>
  <v-container fluid>
    <v-card>
      <v-card-title class="text-h6 pa-4">
        Sample Box Positions View (Read-Only)
      </v-card-title>
      <GenericCrudTable
          ref="tableRef"
          api-path="/sample-boxpos"
          :headers="headers"
          default-sort="s_stamp"
          :createFn="noOp"
          :updateFn="noOp"
          :deleteFn="noOp"
          :getId="row => `${row.sId},${row.sStamp}`"
          :readonly="true"
      />
    </v-card>
  </v-container>
</template>
