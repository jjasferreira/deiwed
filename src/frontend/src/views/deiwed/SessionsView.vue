<template>
  <v-card :flat="$store.getters.isMobile" class="table pb-4">
    <v-card-title>Sessões do DEI às Quartas</v-card-title>
    <v-card-text>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Pesquisar"
        single-line
        hide-details
      ></v-text-field>
      <v-data-table
        :headers="headers"
        :items="sessions"
        :search="search"
        :loading="loading"
        single-select
        locale="pt-PT"
        no-data-text="Não existem participantes registados"
        no-results-text="Nenhuma sessão corresponde aos critérios indicados"
        sort-by="subject"
      >
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
import SessionDto from '@/models/deiwed/SessionDto';
import RemoteServices from '@/services/RemoteServices';
import { Component, Vue } from 'vue-property-decorator';
import { DataTableHeader } from 'vuetify';

@Component
export default class SessionsView extends Vue {
  sessions: SessionDto[] = [];
  headers: DataTableHeader[] = [
    { text: 'ID', value: 'id', sortable: true, filterable: true },
    { text: 'Poster', value: 'poster', sortable: false, filterable: false },
    { text: 'Tema', value: 'subject', sortable: true, filterable: true },
    { text: 'Orador', value: 'speaker', sortable: true, filterable: false },
    { text: 'Data', value: 'date', sortable: true, filterable: true },
    { text: 'Prato normal', value: 'normalDish', sortable: true, filterable: true },
    { text: 'Prato vegetariano', value: 'vegetarianDish', sortable: true, filterable: true },
    { text: 'Participantes', value: 'participants', sortable: false, filterable: false },
  ];
  search = '';
  loading = true;

  async mounted() {
    await this.$store.dispatch('loading');
    try {
      this.sessions = await RemoteServices.getSessions();
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
}
</script>
