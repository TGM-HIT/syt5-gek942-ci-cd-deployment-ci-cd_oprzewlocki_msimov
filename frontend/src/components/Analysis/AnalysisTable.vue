<template>
  <v-card>
    <v-card-title class="justify-space-between">
      <span class="text-h6">Analysis Data</span>
      <v-btn color="primary" @click="openDialog()">+ New Entry</v-btn>
    </v-card-title>

    <v-text-field
      v-model="store.filter"
      label="Search"
      dense
      hide-details
      clearable
      class="mx-4 my-2"
      @keyup.enter="store.fetch"
    ></v-text-field>

    <v-data-table-server
      :headers="headers"
      :items="store.items"
      :items-length="store.total"
      :loading="store.loading"
      :items-per-page="store.size"
      :page="store.page"
      :sort-by="store.sortBy"
      loading-text="Loading..."
      @update:options="updateOptions"
    >
      <template #item.actions="{ item }">
        <v-btn
          icon="mdi-pencil"
          size="small"
          color="blue"
          @click="openDialog(item)"
        ></v-btn>
        <v-btn
          icon="mdi-delete"
          size="small"
          color="red"
          @click="remove(item)"
        ></v-btn>
      </template>
    </v-data-table-server>

    <analysis-form-dialog
      v-model="dialog"
      :edited-item="editedItem"
      @save="saveItem"
    />
  </v-card>
</template>

<script setup>
import { ref } from "vue";
import { useAnalysisStore } from "@/stores/analysis";
import { storeToRefs } from "pinia";
import AnalysisFormDialog from "./AnalysisFormDialog.vue";
import AnalysisService from "@/services/AnalysisService";

const store = useAnalysisStore();
const { items } = storeToRefs(store);
const dialog = ref(false);
const editedItem = ref(null);

const headers = [
  { title: "ID", key: "id" },
  { title: "Sample", key: "sampleId" },
  { title: "Created", key: "created" },
  { title: "Value A", key: "valueA" },
  { title: "Value B", key: "valueB" },
  { title: "Status", key: "status" },
  { title: "Actions", key: "actions", sortable: false },
];

function updateOptions({ page, itemsPerPage, sortBy }) {
  store.page = page;
  store.size = itemsPerPage;
  store.sortBy = sortBy;
  store.fetch();
}

/**
 * Opens the form dialog.
 * If editing, fetch the full record from backend (so nothing gets overwritten).
 */
async function openDialog(item = null) {
  if (item && item.id) {
    try {
      const full = await AnalysisService.getById(item.id);
      // Merge backend data to preserve all fields
      editedItem.value = {
        id: full.aid,
        sampleId: full.sample?.s_id,
        sampleStamp: full.sample?.s_stamp,
        sampleName: full.sample?.name,
        sampleWeightNet: full.sample?.weightNet,
        sampleWeightBru: full.sample?.weightBru,
        sampleWeightTar: full.sample?.weightTar,
        sampleQuantity: full.sample?.quantity,
        sampleDistance: full.sample?.distance,
        sampleDateCrumbled: full.sample?.dateCrumbled,
        sampleFlags: full.sample?.getsFlags,
        sampleLane: full.sample?.lane,
        sampleComment: full.sample?.comment,
        sampleDateExported: full.sample?.dateExported,
        valueA: full.pol,
        valueB: full.nat,
        kal: full.kal,
        an: full.an,
        glu: full.glu,
        dry: full.dry,
        dateIn: full.dateIn,
        dateOut: full.dateOut,
        dateExported: full.dateExported,
        weightMea: full.weightMea,
        weightNrm: full.weightNrm,
        weightCur: full.weightCur,
        weightDif: full.weightDif,
        density: full.density,
        lane: full.lane,
        status: full.comment,
        aflags: full.aflags,
        sample: full.sample,
      };
    } catch (err) {
      console.error("Failed to fetch full record:", err);
      editedItem.value = { ...item }; // fallback: use current table item
    }
  } else {
    // creating new entry
    editedItem.value = {};
  }

  dialog.value = true;
}

/**
 * Saves (create or update) an item
 */
async function saveItem(item) {
  try {
    if (item.id) await store.update(item.id, item);
    else await store.create(item);

    dialog.value = false;
    store.fetch();
  } catch (err) {
    console.error("Save failed", err);
  }
}

/**
 * Deletes an entry
 */
async function remove(item) {
  if (confirm("Delete this entry?")) {
    await store.delete(item.id);
    store.fetch();
  }
}

// initial load
store.fetch();
</script>
