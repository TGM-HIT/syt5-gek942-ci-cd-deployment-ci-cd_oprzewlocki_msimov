import { defineStore } from 'pinia';

export const useTablesStore = defineStore("tables", {
  state: () => ({
    tables: ["samples", "analysis", "box", "thresholds", "boxpos", "log"],

    active: "samples",

    fieldsByTable: {
      samples: ["s_id", "s_stamp", "name", "lane", "comment"],

      analysis: [
        "a_id",
        "s_id", "s_stamp",
        "lane", "comment",
        "pol", "nat", "kal", "an", "glu", "dry",
        "dateIn", "dateOut"
      ],

      box: [                                  // <— NEU
        "b_id",
        "name",
        "num_max",
        "type",
        "comment",
        "date_exported"
      ],

      thresholds: ["thId", "valueMin", "valueMax", "dateChanged"],

      boxpos: [
        "bposId",
        "bId",
        "sId",
        "sStamp",
        "dateExported"
      ],

      log: ["logId","dateCreated","level","info","sId","sStamp","aId","dateExported"]
    }
  }),

  actions: {
    setActive(t: string) {
      this.active = t;
    },
    getFieldsForActive() {
      return this.fieldsByTable[this.active] ?? [];
    }
  }
});
