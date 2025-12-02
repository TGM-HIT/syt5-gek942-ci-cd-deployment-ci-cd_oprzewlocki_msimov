<template>
  <div class="search-bar-container">
    <v-text-field
      v-model="model"
      prepend-inner-icon="mdi-magnify"
      placeholder="Search (doesn't work yet)…"
      hide-details
      class="search-field"
      density="comfortable"
      variant="outlined"
      rounded="xl"
    />

    <div class="links">
      <v-btn
        variant="tonal"
        size="small"
        @click="$router.push(`/table/sample`)"
      >
        Sample
      </v-btn>
      <v-btn
        variant="text"
        size="small"
        href="http://localhost:8040/swagger-ui/index.html"
        target="_blank"
      >
        OpenAPI
      </v-btn>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
const model = ref("");
</script>

<style scoped>
.search-bar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 24px 0;
  gap: 16px;
}

.search-field {
  width: 50%;
  max-width: 600px;
}

.links {
  display: flex;
  gap: 8px;
}
</style>
