<template>
  <v-dialog
    :model-value="modelValue"
    @update:model-value="emit('update:modelValue', $event)"
    max-width="500"
  >
    <v-card>
      <v-card-title>{{
        editedItem.id ? "Edit Entry" : "New Entry"
      }}</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="localItem.sampleId"
          label="Sample ID"
        ></v-text-field>
        <v-text-field
          v-model="localItem.valueA"
          label="Value A"
          type="number"
        ></v-text-field>
        <v-text-field
          v-model="localItem.valueB"
          label="Value B"
          type="number"
        ></v-text-field>
        <v-text-field v-model="localItem.status" label="Status"></v-text-field>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn text @click="close">Cancel</v-btn>
        <v-btn color="primary" @click="save">Save</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  modelValue: Boolean,
  editedItem: Object,
});
const emit = defineEmits(["update:modelValue", "save"]);

const localItem = ref({});
watch(
  () => props.editedItem,
  (val) => {
    localItem.value = val ? { ...val } : {};
  },
  { immediate: true }
);

function close() {
  emit("update:modelValue", false);
}
function save() {
  emit("save", localItem.value);
}
</script>
