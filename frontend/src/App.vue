<template>
  <v-app>
    <AppSidebar @create="onCreate" />
    <v-main>
      <TopBar />
      <router-view v-slot="{ Component }">
        <component :is="Component" ref="tableWrapper" />
      </router-view>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { ref } from "vue";
import AppSidebar from "@/components/layout/AppSidebar.vue";
import TopBar from "@/components/layout/TopBar.vue";

const tableWrapper = ref<any>(null);
const onCreate = () => tableWrapper.value?.openCreate?.();
</script>
