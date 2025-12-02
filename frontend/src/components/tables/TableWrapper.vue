<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import SampleTable from '@/components/tables/SampleTable.vue';
import AnalysisTable from '@/components/tables/AnalysisTable.vue';
import BoxTable from '@/components/tables/BoxTable.vue';
import ThresholdTable from "@/components/tables/ThresholdTable.vue";
import LogTable from "@/components/tables/LogTable.vue";
import BoxPosTable from "@/components/tables/BoxPosTable.vue";

const route = useRoute();
const name = computed(() => route.params.name as string);

const tableMap: Record<string, any> = {
  samples: SampleTable,
  analysis: AnalysisTable,
  box: BoxTable,
  thresholds: ThresholdTable,
  log: LogTable,
  boxpos: BoxPosTable
};

const tableComponent = computed(() => tableMap[name.value] ?? SampleTable);

const child = ref<any>(null);
function openCreate() { child.value?.openCreate?.(); }
defineExpose({ openCreate });
</script>

<template>
  <component :is="tableComponent" ref="child" :key="name" />
</template>
