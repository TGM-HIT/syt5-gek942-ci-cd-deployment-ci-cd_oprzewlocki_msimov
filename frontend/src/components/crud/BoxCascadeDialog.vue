<script setup lang="ts">
import { defineProps, defineEmits } from "vue";

const props = defineProps<{
  visible: boolean;
  boxId?: string;
}>();

const emit = defineEmits<{
  (e: "cancel"): void;
  (e: "confirmCascade"): void;
}>();
</script>

<template>
  <v-dialog v-model="props.visible" width="420">
    <v-card class="pa-4" elevation="10" style="backdrop-filter: blur(10px);">
      <v-card-title class="text-h6 font-weight-bold">
        Box löschen?
      </v-card-title>

      <v-card-text class="text-body-2">
        <p>
          Die Box <strong>{{ props.boxId }}</strong> enthält noch
          <strong>BoxPos</strong>-Einträge.
        </p>
        <p class="mt-2">
          Wenn du fortfährst, werden <strong>alle zugehörigen BoxPos</strong>
          zusammen mit der Box gelöscht.
        </p>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="emit('cancel')">Abbrechen</v-btn>
        <v-btn color="error" variant="flat" @click="emit('confirmCascade')">
          Box + BoxPos löschen
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
