<template>
  <v-navigation-drawer
    width="240"
    permanent
    class="pa-4 elevation-1 d-flex flex-column"
  >
    <v-list density="comfortable" nav>
      <v-list-subheader class="text-uppercase text-subtitle-2 mb-2">
        Tables
      </v-list-subheader>

      <v-list-item
        v-for="t in tables"
        :key="t"
        :to="`/table/${t}`"
        :title="t.charAt(0).toUpperCase() + t.slice(1)"
        class="mb-1 rounded-lg"
        active-class="bg-primary text-white"
        @click="set(t)"
      />
    </v-list>

    <v-spacer />

    <div class="d-flex justify-center pa-3 mt-auto">
      <v-btn
        icon="mdi-plus"
        color="primary"
        elevation="6"
        size="large"
        class="sidebar-create-btn"
        @click="$emit('create')"
      />
    </div>
  </v-navigation-drawer>

  <!-- UNIVERSAL CRUD DIALOG -->
  <CrudDialog
    :visible="showDialog"
    :mode="mode"
    :form="form"
    :fields="fields"
    @cancel="closeDialog"
    @save="handleSave"
  />
</template>

<script setup lang="ts">
import { ref, reactive, computed } from "vue";
import { useTablesStore } from "@/stores/tables.store";
import CrudDialog from "@/components/crud/CrudDialog.vue";

import { createSample } from "@/services/tables/sample.service"; // später pro Tabelle anders

const store = useTablesStore();
const tables = store.tables;
const set = (t: string) => store.setActive(t);

// Dialog State
const showDialog = ref(false);
const mode = ref<"create" | "edit">("create");
const form = ref<Record<string, any>>({});

// Dynamische Felder vom Store (du machst das später schön)
const fields = computed(() => store.getFieldsForActive()); // <--- DU fügst diese Funktion in Store ein



function closeDialog() {
  showDialog.value = false;
}

async function handleSave(localForm) {
  await createSample(localForm);
  closeDialog();
}


</script>

<style scoped>
.v-navigation-drawer {
  border-right: 1px solid rgba(255, 255, 255, 0.08);
  background-color: #121212 !important;
}
.sidebar-create-btn {
  backdrop-filter: blur(8px);
  transition: all 0.25s ease;
}
.sidebar-create-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 0 12px rgba(100, 181, 246, 0.6);
}
</style>
