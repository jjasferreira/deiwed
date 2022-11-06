<template>
  <v-card :flat="$store.getters.isMobile" class="table pb-4">
    <v-card-title>Participantes no DEI às Quartas</v-card-title>
    <v-card-text>
      <v-expansion-panels v-model="panel">
        <v-expansion-panel v-model="panel">
          <v-expansion-panel-header>Adicionar novo participante</v-expansion-panel-header>
          <v-expansion-panel-content>
            <v-form>
              <v-container>
                <v-row>
                  <v-col cols="12" md="3">
                    <v-text-field v-model="new_name" :rules="nameRules" label="Nome" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-text-field v-model="new_istId" :rules="istIdRules" label="IST ID" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-select v-model="new_type" :items="items" :rules="typeRules" label="Tipo" required></v-select>
                  </v-col>
                  <v-col cols="12" md="1" style="text-align: left">
                    <v-btn @click='createAttendee()' class="mx-2" fab dark small color="blue"><v-icon dark>mdi-plus</v-icon></v-btn>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
          </v-expansion-panel-content>
        </v-expansion-panel>
        <v-expansion-panel v-model="panel">
          <v-expansion-panel-header>Editar participante (por ID)</v-expansion-panel-header>
          <v-expansion-panel-content>
            <v-form>
              <v-container>
                <v-row>
                  <v-col cols="12" md="2">
                    <v-text-field v-model="edit_id" :rules="idRules" label="ID" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="3">
                    <v-text-field v-model="edit_name" :rules="nameRules" label="Nome" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-text-field v-model="edit_istId" :rules="istIdRules" label="IST ID" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-select v-model="edit_type" :items="items" :rules="typeRules" label="Tipo" required></v-select>
                  </v-col>
                  <v-col cols="12" md="1" style="text-align: left">
                    <v-btn @click='editAttendee()' class="mx-2" fab dark small color="green"><v-icon dark>mdi-pencil</v-icon></v-btn>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
      <v-text-field v-model="search" append-icon="mdi-magnify" label="Pesquisar" single-line hide-details></v-text-field>
      <v-data-table
        :headers="headers"
        :items="attendees"
        :search="search"
        :loading="loading"
        single-select
        locale="pt-PT"
        no-data-text="Não existem participantes registados"
        no-results-text="Nenhum participante corresponde aos critérios indicados"
        sort-by="name"
      >
        <template v-slot:[`item.type`]="{ item }">
          <v-chip v-if="item.type === 'TEACHER'" color="purple" text-color="white"> Docente </v-chip>
          <v-chip v-else color="green" text-color="white"> Bolseiro </v-chip>
        </template>
        <template v-slot:[`item.actions`]="{ item }">
          <v-btn @click="deleteAttendee(item)" class="mx-2" fab dark x-small color="red" elevation="2"><v-icon dark>mdi-delete</v-icon></v-btn>
        </template>
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
import AttendeeDto from '@/models/deiwed/AttendeeDto';
import RemoteServices from '@/services/RemoteServices';
import { Component, Vue } from 'vue-property-decorator';
import { DataTableHeader } from 'vuetify';
import '@mdi/font/css/materialdesignicons.css';

@Component
export default class AttendeesView extends Vue {

  loading = true;
  panel = false;

  idRules = [(v: string) => !!v || '´É obrigatório indicar um ID'];
  nameRules = [(v: any) => !!v || 'É obrigatório indicar um nome'];
  istIdRules = [(v: any) => !!v || 'É obrigatório indicar um IST ID'];
  typeRules = [(v: any) => !!v || 'É obrigatório indicar um tipo'];

  new_name = '';
  new_istId = '';
  new_type = '';

  items = ['Bolseiro', 'Docente'];
  items_translation: { [key: string]: string } = {'Bolseiro': 'GRANTEE', 'Docente': 'TEACHER'};

  async createAttendee() {
    if (this.new_name === '' || this.new_istId === '' || this.new_type === '') {
      this.$store.dispatch('error', 'É obrigatório preencher todos os campos'); return; }
    await this.$store.dispatch('loading');
    this.panel = false;
    try {
      await RemoteServices.createAttendee({"id":0, "name":this.new_name, "istId":this.new_istId, "type":this.items_translation[this.new_type]});
      this.attendees = await RemoteServices.getAttendees();
      this.loading = false;
      this.new_name = '';
      this.new_istId = '';
      this.new_type = '';
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  search = '';
  attendees: AttendeeDto[] = [];
  headers: DataTableHeader[] = [
    { text: 'ID', value: 'id', sortable: true, filterable: true },
    { text: 'Nome', value: 'name', sortable: true, filterable: true },
    { text: 'IST ID', value: 'istId', sortable: true, filterable: true },
    { text: 'Tipo', value: 'type', sortable: true, filterable: false },
    { text: 'Eliminar', value: 'actions', sortable: false, filterable: false },
  ];

  async mounted() {
    await this.$store.dispatch('loading');
    try {
      this.attendees = await RemoteServices.getAttendees();
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  edit_id = 0;
  edit_name = '';
  edit_istId = '';
  edit_type = '';

  async editAttendee() {
    this.panel = false;
    await this.$store.dispatch('loading');
    try {
      await RemoteServices.editAttendee({"id":this.edit_id, "name":this.edit_name, "istId":this.edit_istId, "type":this.items_translation[this.edit_type]});
      this.attendees = await RemoteServices.getAttendees();
      this.loading = false;
      this.edit_id = 0;
      this.edit_name = '';
      this.edit_istId = '';
      this.edit_type = '';
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async deleteAttendee(attendee: AttendeeDto) {
    await this.$store.dispatch('loading');
    try {
      await RemoteServices.deleteAttendee(attendee.id);
      this.attendees = await RemoteServices.getAttendees();
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
}

</script>