<script setup lang="ts">
import { ref, watch, onMounted, onBeforeUnmount, computed, readonly } from "vue";
import { http } from "@/services/http";
import CrudDialog from "./CrudDialog.vue";
import DeleteChoiceDialog from "./DeleteChoiceDialog.vue";


const props = defineProps<{
  apiPath: string;
  headers: { key: string; title: string }[];
  defaultSort?: string;
  getId?: (row: Record<string, any>) => string;
  createFn: (form: any) => Promise<any>;
  updateFn: (form: any) => Promise<any>;
  deleteFn: (id: any, row?: any) => Promise<any>;
  keyMap?: Record<string, string>;
}>();

const isSampleTable = computed(() => props.apiPath.includes("/samples"));

const emit = defineEmits<{
  (e: "created", row: any): void;
  (e: "updated", row: any): void;
  (e: "deleted", id: string): void;
}>();

// ========== State ==========
const rows = ref<any[]>([]);
const total = ref(0);
const loading = ref(false);
const page = ref(1);
const sortBy = ref<{ key: string; order: "asc" | "desc" }[]>([
  { key: props.defaultSort || props.headers[0].key, order: "desc" },
]);

// Filters (auto-created from headers)
const filters = ref<Record<string, string>>(
  Object.fromEntries(props.headers.map((h) => [h.key, ""]))
);

const visibleFields = computed(() => {
  const keys = props.headers.map(h => h.key);
  if (dialog.value.mode === 'create') {
    if (props.apiPath.includes('/analysis')) return keys.filter(k => k !== 'aId');
    return keys;
  }
  return keys;
});

// Dynamic rows
const itemsPerPage = ref(25);
const userChangedItemsPerPage = ref(false);

function recalcItemsPerPage() {
  const headerHeight = 250; // filters + headers
  const rowHeight = 42;
  const available = window.innerHeight - headerHeight;
  const auto = Math.max(5, Math.floor(available / rowHeight) - 2);
  if (!userChangedItemsPerPage.value) itemsPerPage.value = auto;
}

// Dialogs
const dialog = ref({
  visible: false,
  mode: "create" as "create" | "edit",
  form: {} as Record<string, any>,
});
const confirm = ref({ visible: false, target: null as Record<string, any> | null });
const boxDelete = ref({ visible: false, target: null as Record<string, any> | null });
const advancedDelete = ref({ visible: false, target: null as Record<string, any> | null });
const boxCascade = ref({ visible: false, target: null as any });


// ========== Lifecycle ==========
onMounted(() => {
  recalcItemsPerPage();
  window.addEventListener("resize", recalcItemsPerPage);
});
onBeforeUnmount(() => {
  window.removeEventListener("resize", recalcItemsPerPage);
});

// ========== Data Loading ==========
function mapKey(key: string) {
  // SAMPLE (composite)
  if (key === 'sId') return 'sample.id.sId';
  if (key === 'sStamp') return 'sample.id.sStamp';

  // ANALYSIS (nothing special)
  
  // **BOXPOS (composite ID)**
  if (props.apiPath.includes("/boxpos")) {
    if (key === "bposId") return "id.bposId";
    if (key === "bId") return "id.bId";
  }

  // BOX (simple field)
  if (props.apiPath.includes("/box")) {
    // Make sure "b_id" sorting works
    if (key === "bId") return "bId";
  }

  return key;
}



async function load() {
  loading.value = true;
  try {
    const sort = sortBy.value[0] ?? { key: props.headers[0].key, order: "asc" };

    const mappedFilters: Record<string, string> = {};
    for (const [k, v] of Object.entries(filters.value)) {
      if (v) mappedFilters[mapKey(k)] = v;
    }

    const mappedSort = { [mapKey(sort.key)]: sort.order };

    const { data } = await http.get(props.apiPath, {
      params: {
        page: page.value - 1,
        size: itemsPerPage.value,
        filter: mappedFilters,
        sort: mappedSort,
      },
    });

    function normalizeRow(a: any) {
  const id = a.id ?? {};

  return {
    ...a,

    // ===== Normalize ID naming across all tables =====
    bId: (
      a.bId ??
      id.bId ??
      a.bid ?? 
      id.bid ??
      a.b_id ??
      a.BID ??
      null
    ),

    // ===== normalize BoxPos ID =====
    bposId: a.bposId ?? id.bposId ?? null,

    // ===== normalize Sample foreign keys =====
    sId: (
      a.sId ??
      a.s_id ??
      a.sample?.id?.sId ??
      a.sample?.s_id ??
      null
    ),
    sStamp: (
      a.sStamp ??
      a.s_stamp ??
      a.sample?.id?.sStamp ??
      a.sample?.s_stamp ??
      null
    ),

    // ===== Date normalization =====
    dateExported: a.dateExported ?? a.date_exported ?? null,

    id // keep raw but never display directly
  };
}

  rows.value = (data.content ?? []).map(normalizeRow);



    total.value = data.totalElements ?? 0;

  } catch (err) {
    console.error("Load failed:", err);
  } finally {
    loading.value = false;
  }
}

const readonlyKeys = computed(() => {
  if (props.apiPath.includes('/samples')) return ['s_id','s_stamp'];
  if (props.apiPath.includes('/analysis')) return ['aId'];
  if (props.apiPath === '/box') return ['bId'];
  if (props.apiPath === '/boxpos') return [];
  const autoIds = props.headers.map(h=>h.key).filter(k => k==='id');
  return autoIds;
});



watch([page, itemsPerPage, sortBy, filters], load, { deep: true, immediate: true });

// ========== CRUD Actions ==========
function openCreate(defaults?: Record<string, any>) {
  dialog.value.mode = "create";

  const base = props.apiPath.includes("/samples")
    ? {
        s_id: String(Math.floor(Math.random() * 1e13)).padStart(13, "0"),
        s_stamp: new Date().toISOString().slice(0, 19),
      }
    : {};

  dialog.value.form = { ...base, ...(defaults ?? {}) }; // <- DEFAULTS HIER REIN
  dialog.value.visible = true;
}

defineExpose({ openCreate });

function openEdit(item: any) {
  dialog.value = { visible: true, mode: "edit", form: { ...item } };
}


async function save(payload?: any) {
  const mode = dialog.value.mode;
  const form = payload ?? dialog.value.form;

  try {
    if (mode === "create") {
      console.log("SAVE FORM = ", JSON.stringify(form, null, 2));
      await props.createFn(form);
      emit("created", form);
    } else {
      await props.updateFn(form);
      emit("updated", form);
    }
    dialog.value.visible = false;
    load();
  } catch (err) {
    console.error("Save failed:", err);
  }
}

function updateItemsPerPage(v: number | boolean) {
  userChangedItemsPerPage.value = true;
  if (v !== false) itemsPerPage.value = Number(v);
}


async function remove() {
  const target = confirm.value.target;
  if (!target) return;

  try {
    // === Normaler Delete Versuch ===
    await props.deleteFn(props.getId ? props.getId(target) : target.id, target);

    confirm.value.visible = false;
    load();
  } catch (err: any) {
    console.warn("Delete failed:", err);

    const status = err.response?.status;

    if (
      props.apiPath.includes("/box") &&
      (err?.code === "NEEDS_CASCADE" || status === 409)
    ) {

      console.log(target)
      confirm.value.visible = false;

      const ok = window.confirm(
        `Die Box "${target.bId}" hat noch BoxPos Einträge.\n` +
        `Willst du die Box *komplett inkl. allen BoxPos löschen?*\n\n` +
        `→ OK = CASCADE\n→ Cancel = nix passiert`
      );

      if (ok) {
        await http.delete(`/box/advanced/${target.bId}`, {
          params: { action: "cascade" }
        });
        load();
      }
      return;
    }

    if (props.apiPath.includes("/samples") && status === 409) {
      confirm.value.visible = false;
      advancedDelete.value = { visible: true, target };
      return;
    }

    alert("löschen blockiert. Da hängt noch irgendwas dran.");
  }
}



async function performAdvancedDelete(action: "cascade" | "detach") {
  const t = advancedDelete.value.target;
  if (!t) return;

  if (props.apiPath.includes("/box")) {
    await http.delete(`/api/box/advanced/${t.bId}`, { params: { action } });
    advancedDelete.value.visible = false;
    load();
    return;
  }

  try {
    await http.delete(`/samples/advanced/${t.s_id},${t.s_stamp}`, {
      params: { action },
    });
    advancedDelete.value.visible = false;
    load();
  } catch (err: any) {
    console.error("Advanced delete failed", err);

    const raw = err.response?.data;
    const message =
      typeof raw === "string"
        ? raw
        : typeof raw === "object"
          ? JSON.stringify(raw)
          : "";

    // --- Linked analyses ---
    if (err.response?.status === 409 && message.includes("analysis")) {
      // Show the same analysis popup again
      advancedDelete.value = { visible: true, target: t };
      return;
    }

    // --- Linked boxes (new) ---
    if (err.response?.status === 409 && message.includes("boxpos")) {
      // Show box popup
      boxDelete.value = { visible: true, target: t };
      return;
    }

    // --- Fallback ---
    if (err.response?.status === 409) {
      alert("Delete blocked — related data (analysis or boxpos) still exists.");
      return;
    }

    // Anything else
    console.error("Unexpected delete error:", err);
  }
}


function askDelete(item: any) {
  confirm.value = { visible: true, target: item };
}

</script>

<template>
  <v-card flat>
    <v-data-table-server
      :headers="[...props.headers, { key: 'actions', title: 'Actions' }]"
      :items="rows"
      :items-length="total"
      :loading="loading"
      :page="page"
      :items-per-page="itemsPerPage"
      :sort-by="sortBy"
      @update:page="page = $event"
      @update:items-per-page="updateItemsPerPage"
      @update:sort-by="sortBy = $event"
      density="comfortable"
    >
      <!-- Filters -->
      <template #top>
        <v-row class="pa-2" align="center">
          <v-col v-for="h in props.headers" :key="h.key" cols="12" sm="2">
            <v-text-field
              :label="`Filter ${h.key}`"
              v-model="filters[h.key]"
              variant="outlined"
              density="comfortable"
              hide-details
              clearable
            />
          </v-col>
        </v-row>
      </template>

      <!-- Rows -->
      <template #item="{ item }">
        <tr>
          <td v-for="h in props.headers" :key="h.key">{{ item[h.key] }}</td>
          <td class="text-right">
            <v-btn icon="mdi-pencil" size="small" color="primary" variant="text" @click="openEdit(item)" />
            <v-btn icon="mdi-delete" size="small" color="error" variant="text" @click="askDelete(item)" />
          </td>
        </tr>
      </template>
    </v-data-table-server>

    <!-- Create/Edit Dialog -->
    <CrudDialog
      v-model:visible="dialog.visible"
      :mode="dialog.mode"
      :form="dialog.form"
      :fields="visibleFields"
      :readonlyKeys="readonlyKeys"
      @save="save"
      @cancel="dialog.visible = false"
    />




    <!-- Delete Confirmation -->
    <v-dialog v-model="confirm.visible" width="420">
      <v-card class="pa-4" style="backdrop-filter: blur(10px);">
        <v-card-title>Delete item?</v-card-title>
        <v-card-text>
          This action cannot be undone.
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn variant="text" @click="confirm.visible = false">Cancel</v-btn>
          <v-btn color="error" variant="flat" @click="remove">Delete</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <DeleteChoiceDialog
      v-model:visible="advancedDelete.visible"
      :target-name="advancedDelete.target?.name"
      @cancel="advancedDelete.visible = false"
      @choose="performAdvancedDelete"
    />

  </v-card>
</template>
