<!-- src/components/crud/DeleteChoiceDialog.vue -->
<script setup lang="ts">
import { defineProps, defineEmits } from "vue";

const props = defineProps<{
  visible: boolean;
  targetName?: string;
  mode?: "sample" | "box";
}>();

const emit = defineEmits<{
  (e: "cancel"): void;
  (e: "choose", action: "cascade" | "detach"): void;
}>();
</script>

<template>
  <v-dialog v-model="props.visible" width="480">
    <v-card class="pa-4" elevation="10" style="backdrop-filter: blur(10px);">
      <v-card-title class="text-h6 font-weight-bold">
        Linked Analyses Detected
      </v-card-title>

      <v-card-text>
        <p class="text-body-2">
          The sample
          <strong>{{ props.targetName || "this sample" }}</strong>
          is still referenced by one or more analyses and/or boxes.
          Boxes cannot exist without a sample.
        </p>
        <p class="text-body-2">How would you like to proceed?</p>
      </v-card-text>

      <v-card-actions class="flex-column ga-2">
        <v-btn block variant="flat" color="error" @click="emit('choose', 'cascade')">
          Delete sample and related analyses & boxes
        </v-btn>
        <v-btn block variant="flat" color="primary" @click="emit('choose', 'detach')">
          Detach analyses and delete sample & boxes
        </v-btn>
        <v-btn block variant="text" color="default" @click="emit('cancel')">
          Cancel
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
