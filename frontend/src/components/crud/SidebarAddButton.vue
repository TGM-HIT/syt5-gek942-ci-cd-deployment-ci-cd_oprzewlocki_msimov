<script setup lang="ts">
import { defineEmits, onMounted, ref } from 'vue';

const emit = defineEmits(['create']);
const sidebar = ref<HTMLElement | null>(null);

onMounted(() => {
  sidebar.value = document.querySelector('.v-navigation-drawer, .sidebar');
});
</script>

<template>
  <div
    style="
      position: absolute;
      bottom: 24px;
      left: 50%;
      transform: translateX(-50%);
      width: 80%;
      display: flex;
      justify-content: center;
    "
  >
    <v-btn
      icon="mdi-plus"
      color="primary"
      size="large"
      elevation="8"
      style="backdrop-filter: blur(12px);"
      @click="emit('create')"
    />
  </div>
</template>
