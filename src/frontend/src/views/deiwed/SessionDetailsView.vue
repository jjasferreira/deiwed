<template>
  <v-card :flat="$store.getters.isMobile" class="table pb-4">
    <v-card-title> Detalhes da Sessão de ID {{ $route.params.sessionId }} </v-card-title>
    <v-card-text>
      <div class="mb-10">
        <h3 class="mb-2">Tema</h3>
        {{ session.subject }}
      </div>
      <v-row class="mb-10 d-flex justify-center">
        <v-col cols="12" md="4">
          <h3 class="mb-2">Orador</h3>
          {{ session.speaker }}
        </v-col>
        <v-col cols="12" md="4">
          <h3 class="mb-2">Data</h3>
          {{ session.date }}
        </v-col>
      </v-row>


      <h3 class="mb-2">Pratos normal e vegetariano para o almoço</h3>
      <v-data-table class="mb-10"
        :disable-pagination="true"
        :hide-default-footer="true"
        :headers="dishes_headers"
        :items="[normalDish, vegetarianDish]"
        :loading="loading"
        locale="pt-PT"
        no-data-text="Não existem pratos selecionados"
      >
        <template v-slot:[`item.vegetarian`]="{ item }">
          <v-icon v-if="item.vegetarian === true" color="green"> mdi-leaf </v-icon>
          <v-icon v-else> mdi-leaf-off </v-icon>
        </template>
      </v-data-table>


      <h3 class="mb-2">Participantes inscritos</h3>
      <v-expansion-panels v-model="panel">
        <v-expansion-panel v-model="panel">
          <v-expansion-panel-header>Inscrever novo participante na sessão</v-expansion-panel-header>
          <v-expansion-panel-content>
            <v-form>
              <v-container>
                <v-row>
                  <v-col cols="12" md="5">
                    <v-select v-model="new_participant_name" :items="participantsNotRegisteredNames" :rules="participantRules" label="Participante" required></v-select>
                  </v-col>
                  <v-col cols="12" md="1" style="text-align: left">
                    <v-btn @click='addAttendeeToSession()' class="mx-2" fab dark small color="blue"><v-icon dark>mdi-plus</v-icon></v-btn>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
      <v-data-table
        :headers="participants_headers"
        :items="participants"
        :search="search"
        :loading="loading"
        single-select
        locale="pt-PT"
        no-data-text="Não existem participantes inscritos"
        no-results-text="Nenhum participante corresponde aos critérios indicados"
        sort-by="name"
      >
        <template v-slot:[`item.type`]="{ item }">
          <v-chip v-if="item.type === 'TEACHER'" color="purple" text-color="white"> Docente </v-chip>
          <v-chip v-else color="green" text-color="white"> Bolseiro </v-chip>
        </template>
        <template v-slot:[`item.actions`]="{ item }">
          <v-btn @click="removeAttendeeFromSession(item.id)" class="mx-2" fab dark x-small color="red" elevation="2"><v-icon dark>mdi-close</v-icon></v-btn>
        </template>
      </v-data-table>


      <h3 class="mb-2">Eliminar sessão</h3>
      <v-btn @click="deleteSession(parseInt($route.params.sessionId))" elevation="2" color="red" outlined> Eliminar <v-icon right dark>mdi-delete</v-icon></v-btn>
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
export default class SessionDetailsView extends Vue {

  participantRules: any[] = [(v: string) => !!v || 'É obrigatório selecionar um participante'];
  search = '';
  loading = true;
  panel = false;
  session: SessionDto = new SessionDto();
  normalDish: DishDto = new DishDto();
  vegetarianDish: DishDto = new DishDto();
  participants: Array<AttendeeDto> = [];
  participantsNotRegistered: Array<AttendeeDto> = [];
  participantsNotRegisteredNames: Array<string> = [];
  new_participant_name = '';

  dishes_headers: DataTableHeader[] = [
    { text: 'ID', value: 'id', sortable: false, filterable: false },
    { text: 'Nome', value: 'name', sortable: false, filterable: false },
    { text: 'Descrição', value: 'description', sortable: false, filterable: false },
    { text: 'Preço p/ uni. (€)', value: 'unitPrice', sortable: false, filterable: false },
    { text: 'Peso p/ uni (kg)', value: 'unitWeight', sortable: false, filterable: false },
    { text: 'Vegetariano', value: 'vegetarian', sortable: false, filterable: false },
  ];

  participants_headers: DataTableHeader[] = [
    { text: 'ID', value: 'id', sortable: true, filterable: true },
    { text: 'Nome', value: 'name', sortable: true, filterable: true },
    { text: 'IST ID', value: 'istId', sortable: true, filterable: true },
    { text: 'Tipo', value: 'type', sortable: true, filterable: true },
    { text: 'Desinscrever da sessão', value: 'actions', sortable: false, filterable: false },
  ];

  async mounted() {
    await this.$store.dispatch('loading');
    try {
      let sessionId = this.sessionId();
      this.session = await RemoteServices.getSession(sessionId);
      this.normalDish = await RemoteServices.getDish(this.session.normalDishId);
      this.vegetarianDish = await RemoteServices.getDish(this.session.vegetarianDishId);
      this.updateSession();
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  sessionId() {
    return parseInt(this.$route.params.sessionId);
  }

  async updateSession() {
    this.participants = []; this.participantsNotRegistered = []; this.participantsNotRegisteredNames = [];
    let sessionId = this.sessionId();
    this.session = await RemoteServices.getSession(sessionId);
    for (let participantId of this.session.participantsIds) {
        let participant = await RemoteServices.getAttendee(participantId);
        this.participants.push(participant);}
      this.participantsNotRegistered = await RemoteServices.getAttendees();
    // Remove participants already registered from the list of participants not registered
    for (let participant of this.participants) {
        this.participantsNotRegistered = this.participantsNotRegistered.filter((a) => a.id !== participant.id);
      }
    // Create a list of names of participants not registered
    for (let participant of this.participantsNotRegistered) {
      this.participantsNotRegisteredNames.push(participant.name);
    }
  }

  async addAttendeeToSession() {
    await this.$store.dispatch('loading');
    try {
      let new_participantId = 0;
      for (const a of this.participantsNotRegistered) {if (a.name === this.new_participant_name) {new_participantId = a.id; break;}}
      let new_participantsIds = this.session.participantsIds
      new_participantsIds.push(new_participantId);
      await RemoteServices.updateSession({"id": this.session.id, "subject": this.session.subject, "speaker": this.session.speaker, "date": this.session.date,
      "normalDishId": this.session.normalDishId, "vegetarianDishId": this.session.normalDishId, "participantsIds": new_participantsIds});
      this.updateSession();
      this.new_participant_name = '';
      this.panel = false;
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async removeAttendeeFromSession(attendeeId: number) {
    await this.$store.dispatch('loading');
    try {
      let new_participantsIds = this.session.participantsIds
      new_participantsIds = new_participantsIds.filter((a) => a !== attendeeId);
      await RemoteServices.updateSession({"id": this.session.id, "subject": this.session.subject, "speaker": this.session.speaker, "date": this.session.date,
      "normalDishId": this.session.normalDishId, "vegetarianDishId": this.session.normalDishId, "participantsIds": new_participantsIds});
      this.updateSession();
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async deleteSession(sessionId: number) {
    await this.$store.dispatch('loading');
    try {
      await RemoteServices.deleteSession(sessionId);
      this.$router.push('/sessions');
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

}
</script>
