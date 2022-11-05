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
                  <v-col cols="12" md="2">
                    <v-file-input accept="image/*" prepend-icon="mdi-camera" label="Poster (opcional)"></v-file-input>
                  </v-col>
                  <v-col cols="12" md="4">
                    <v-text-field v-model="new_subject" :rules="subjectRules" label="Tema" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="3">
                    <v-text-field v-model="new_speaker" label="Orador" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-text-field v-model="new_date" :rules="dateRules" label="Data" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="3">
                    <v-select v-model="new_normalDishName" :items="items_normalDishNames" :rules="normalDishRules" label="Prato normal" required></v-select>
                  </v-col>
                  <v-col cols="12" md="3">
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
        no-data-text="Não existem participantes registados"
        no-results-text="Nenhuma sessão corresponde aos critérios indicados"
        sort-by="subject"
      >
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

  items_normalDish: DishDto[] = [];
  items_normalDishNames: string[] = [];
  items_vegetarianDish: DishDto[] = [];
  items_vegetarianDishNames: string[] = [];
  items_participants: AttendeeDto[] = [];

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
  panel = false;

  idRules = [(v: string) => !!v || '´É obrigatório indicar um ID'];
  nameRules = [(v: any) => !!v || 'É obrigatório indicar um nome'];
  subjectRules = [(v: any) => !!v || 'É obrigatório indicar um tema'];
  speakerRules = [(v: any) => !!v || 'É obrigatório indicar um orador'];
  dateRules = [(v: any) => !!v || 'É obrigatório indicar uma data'];
  normalDishRules = [(v: any) => !!v || 'É obrigatório indicar um prato normal'];
  vegetarianDishRules = [(v: any) => !!v || 'É obrigatório indicar um prato vegetariano'];

  new_poster = undefined;
  new_subject = '';
  new_speaker = '';
  new_date = '';
  new_normalDish = new DishDto();
  new_normalDishName = '';
  new_vegetarianDish = new DishDto();
  new_vegetarianDishName = '';
  new_participants = [];

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
    this.panel = false;
    await this.$store.dispatch('loading');
    try {
      if (this.new_subject === '' || this.new_speaker === '' || this.new_date === '' || this.new_normalDish.name === '' || this.new_vegetarianDish.name === '') {
        this.$store.dispatch('error', 'É obrigatório preencher todos os campos');
        await this.$store.dispatch('clearLoading');
        return;
      }
      for (let i = 0; i < this.items_normalDishNames.length; i++) {
        if (this.new_normalDishName === this.items_normalDishNames[i]) {this.new_normalDish = this.items_normalDish[i]; break; }}
      for (let i = 0; i < this.items_vegetarianDishNames.length; i++) {
        if (this.new_vegetarianDishName === this.items_vegetarianDishNames[i]) {this.new_vegetarianDish = this.items_vegetarianDish[i]; break; }}
      await RemoteServices.createSession({"id":0, "poster":this.new_poster, "subject":this.new_subject, "speaker":this.new_speaker, "date":this.new_date, "normalDish":this.new_normalDish, "vegetarianDish":this.new_vegetarianDish, "participants":this.new_participants});
      this.sessions = await RemoteServices.getSessions();
      this.loading = false;
      this.new_poster = undefined;
      this.new_subject = '';
      this.new_speaker = '';
      this.new_date = '';
      this.new_normalDish = new DishDto();
      this.new_vegetarianDish = new DishDto();
      this.new_participants = [];
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

}
</script>
