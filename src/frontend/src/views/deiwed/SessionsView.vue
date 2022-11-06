<template>
  <v-card :flat="$store.getters.isMobile" class="table pb-4">
    <v-card-title>Sessões do DEI às Quartas</v-card-title>
    <v-card-text>
      <v-expansion-panels v-model="panel">
        <v-expansion-panel v-model="panel">
          <v-expansion-panel-header>Adicionar nova sessão</v-expansion-panel-header>
          <v-expansion-panel-content>
            <v-form>
              <v-container>
                <v-row>
                  <v-col cols="12" md="3">
                    <v-text-field v-model="new_subject" :rules="subjectRules" label="Tema" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-text-field v-model="new_speaker" label="Orador" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-text-field v-model="new_date" :rules="dateRules" label="Data (YYYY-MM-DD)" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-select v-model="new_normalDishName" :items="items_normalDishNames" :rules="normalDishRules" label="Prato normal" required></v-select>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-select v-model="new_vegetarianDishName" :items="items_vegetarianDishNames" :rules="vegetarianDishRules" label="Prato vegetariano" required></v-select>
                  </v-col>
                  <v-col cols="12" md="1" style="text-align: left">
                    <v-btn @click='createSession()' class="mx-2" fab dark small color="blue"><v-icon dark>mdi-plus</v-icon></v-btn>
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
        :items="sessions"
        :search="search"
        :loading="loading"
        single-select
        locale="pt-PT"
        no-data-text="Não existem sessões registadas"
        no-results-text="Nenhuma sessão corresponde aos critérios indicados"
        sort-by="subject"
      >
        <template v-slot:[`item.access`]="{ item }">
          <v-btn @click="loadSessionDetails(item)" class="mx-2" fab dark x-small color="purple" elevation="2"><v-icon dark>mdi-arrow-right</v-icon></v-btn>
        </template>
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
import AttendeeDto from '@/models/deiwed/AttendeeDto';
import DishDto from '@/models/deiwed/DishDto';
import SessionDto from '@/models/deiwed/SessionDto';
import RemoteServices from '@/services/RemoteServices';
import { Component, Vue } from 'vue-property-decorator';
import { DataTableHeader } from 'vuetify';

@Component
export default class SessionsView extends Vue {

  items_normalDish: Array<DishDto> = [];
  items_normalDishNames: Array<string> = [];
  new_normalDishName = '';
  items_vegetarianDish: Array<DishDto> = [];
  items_vegetarianDishNames: Array<string> = [];
  new_vegetarianDishName = '';
  items_participants: Array<AttendeeDto> = [];
  new_participants: Array<AttendeeDto> = [];
  new_subject = '';
  new_speaker = '';
  new_date = '';

  sessions: SessionDto[] = [];
  headers: DataTableHeader[] = [
    { text: 'ID', value: 'id', sortable: true, filterable: true },
    { text: 'Tema', value: 'subject', sortable: true, filterable: true },
    { text: 'Orador', value: 'speaker', sortable: true, filterable: true },
    { text: 'Data', value: 'date', sortable: true, filterable: true },
    { text: 'Aceder', value: 'access', sortable: false, filterable: false },
  ];

  search = '';
  loading = true;
  panel = false;

  idRules = [(v: string) => !!v || '´É obrigatório indicar um ID'];
  nameRules = [(v: any) => !!v || 'É obrigatório indicar um nome'];
  subjectRules = [(v: any) => !!v || 'É obrigatório indicar um tema'];
  speakerRules = [(v: any) => !!v || 'É obrigatório indicar um orador'];
  dateRules = [(v: any) => !!v || 'É obrigatório indicar uma data'];
  normalDishRules = [(v: any) => !!v || 'É obrigatório indicar um prato normal'];
  vegetarianDishRules = [(v: any) => !!v || 'É obrigatório indicar um prato vegetariano'];

  async mounted() {
    await this.$store.dispatch('loading');
    try {
      this.items_normalDish = await RemoteServices.getNormalDishes();
      this.items_normalDishNames = this.items_normalDish.map((dish) => dish.name);
      this.items_vegetarianDish = await RemoteServices.getVegetarianDishes()
      this.items_vegetarianDishNames = this.items_vegetarianDish.map((dish) => dish.name);
      this.items_participants = await RemoteServices.getAttendees();
      this.sessions = await RemoteServices.getSessions();
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async createSession() {
    if (this.new_subject === '' || this.new_speaker === '' || this.new_date === '' || this.new_normalDishName === '' || this.new_vegetarianDishName === '') {
      this.$store.dispatch('error', 'Por favor preencha todos os campos'); return; }
    if (!/^\d{4}-\d{2}-\d{2}$/.test(this.new_date)) {
      this.$store.dispatch('error', 'A data deve ter o formato YYYY-MM-DD'); return; }
    await this.$store.dispatch('loading');
    this.panel = false;
    try {
      let new_normalDishId = 0; let new_vegetarianDishId = 0; let new_participantsIds: Array<number> = [];
      for (const d of this.items_normalDish) {if (d.name === this.new_normalDishName) {new_normalDishId = d.id; break;}}
      for (const d of this.items_vegetarianDish) {if (d.name === this.new_vegetarianDishName) {new_vegetarianDishId = d.id; break;}}
      await RemoteServices.createSession({"id": 0, "subject": this.new_subject, "speaker": this.new_speaker,
      "date": this.new_date, "normalDishId": new_normalDishId, "vegetarianDishId": new_vegetarianDishId, "participantsIds": new_participantsIds});
      this.sessions = await RemoteServices.getSessions();
      this.loading = false;
      this.new_subject = '';
      this.new_speaker = '';
      this.new_date = '';
      this.new_normalDishName = '';
      this.new_vegetarianDishName = '';
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async loadSessionDetails(session: SessionDto) {
    this.$router.push({ name: 'session_details', params: { sessionId: session.id.toString() } });
  }

}
</script>
