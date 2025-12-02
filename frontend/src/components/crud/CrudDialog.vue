<script setup lang="ts">
import { reactive, watch, computed, ref } from "vue";
import { fetchLatestBox, nextBoxIdFrom } from "../../services/tables/box.service";

const props = defineProps<{
  visible: boolean;
  mode: "create" | "edit";
  form: Record<string, any>;
  fields: string[];
  readonlyKeys?: string[]; 
}>();

const emit = defineEmits<{
  (e: "update:visible", v: boolean): void;  // <-- v-model support
  (e: "save", payload: Record<string, any>): void;
  (e: "cancel"): void;
}>();

const tableRef = ref<any>(null);

defineExpose({
  async openCreate() {
    const latest = await fetchLatestBox();
    const lastId = latest?.bId ?? latest?.bid ?? latest?.b_id;
    const nextId = nextBoxIdFrom(lastId);

    tableRef.value?.openCreate?.({
      bId: nextId,
      dateExported: new Date().toISOString().slice(0, 19),
      numMax: 40,
      type: 1,
    });
  }
});


// v-model Proxy, damit dein <CrudDialog v-model:visible="..."> wieder tut
const visibleProxy = computed({
  get: () => props.visible,
  set: (v: boolean) => {
    if (!v) emit("cancel");
    emit("update:visible", v);
  },
});

// Lokale, saubere Kopie (keine Props mutieren)
const local = reactive<Record<string, any>>({});

// Nur beim Öffnen kopieren, beim Schließen nicht anfassen
watch(
  () => props.visible,
  (v) => {
    if (v) {
      Object.keys(local).forEach(k => delete (local as any)[k]);
      Object.assign(local, JSON.parse(JSON.stringify(props.form)));
    }
  }
);

function onSave() {
  emit("save", JSON.parse(JSON.stringify(local)));
}
function onCancel() {
  emit("cancel");                 // parent schließt
  emit("update:visible", false);  // v-model sync
}
</script>

<template>
  <v-dialog v-model="visibleProxy" width="600" persistent>
    <v-card class="pa-4" elevation="10">
      <v-card-title class="text-h6 font-weight-bold">
        {{ props.mode === "create" ? "Add New Entry" : "Edit Entry" }}
      </v-card-title>

      <v-card-text>
        <v-row>
          <v-col v-for="field in props.fields" :key="field" cols="12" sm="6">
            <v-text-field
              v-model="local[field]"
              :label="field"
              variant="outlined"
              :readonly="props.readonlyKeys?.includes(field)"
            />
          </v-col>
        </v-row>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="onCancel">Cancel</v-btn>
        <v-btn color="primary" variant="flat" @click="onSave">
          {{ props.mode === "create" ? "Create" : "Save" }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
