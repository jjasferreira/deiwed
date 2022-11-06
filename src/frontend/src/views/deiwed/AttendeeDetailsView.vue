<template>
  <v-card :flat="$store.getters.isMobile" class="table pb-4">
    <v-card-title> Detalhes do Participante de ID {{ $route.params.attendeeId }} </v-card-title>
    <v-card-text>
      <div class="mb-10">
        <h3 class="mb-2">Nome</h3>
        {{ attendee.name }}
      </div>
      <v-row class="mb-10 d-flex justify-center">
        <v-col cols="12" md="4">
          <h3 class="mb-2">IST ID</h3>
          {{ attendee.istId }}
        </v-col>
        <v-col cols="12" md="4">
          <h3 class="mb-2">Tipo</h3>
          <v-chip v-if="attendee.type === 'TEACHER'" color="purple" text-color="white"> Docente </v-chip>
          <v-chip v-else color="green" text-color="white"> Bolseiro </v-chip>
        </v-col>
      </v-row>


      <h3 class="mb-2">Sessões a que está inscrito</h3>
      <v-data-table class="mb-10"
        :headers="sessions_headers"
        :items="sessionsRegistered"
        :search="search"
        :loading="loading"
        single-select
        locale="pt-PT"
        no-data-text="O participante não está inscrito a nenhuma sessão"
        no-results-text="Nenhuma sessão corresponde aos critérios indicados"
        sort-by="subject"
      >
        <template v-slot:[`item.actions`]="{ item }">
          <v-btn @click="removeAttendeeFromSession(item)" class="mx-2" fab dark x-small color="red" elevation="2"><v-icon dark>mdi-close</v-icon></v-btn>
        </template>
      </v-data-table>


      <h3 class="mb-2">Eliminar participante</h3>
      <v-btn @click="deleteAttendee()" elevation="2" color="red" outlined> Eliminar <v-icon right dark>mdi-delete</v-icon></v-btn>
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
import AttendeeDto from '@/models/deiwed/AttendeeDto';
import SessionDto from '@/models/deiwed/SessionDto';
import RemoteServices from '@/services/RemoteServices';
import { Component, Vue } from 'vue-property-decorator';
import { DataTableHeader } from 'vuetify';

@Component
export default class SessionDetailsView extends Vue {

  search = '';
  loading = true;
  panel = false;
  attendee: AttendeeDto = new AttendeeDto();
  sessionsRegistered: SessionDto[] = [];

  sessions_headers: DataTableHeader[] = [
    { text: 'ID', value: 'id', sortable: true, filterable: true },
    { text: 'Tema', value: 'subject', sortable: true, filterable: true },
    { text: 'Orador', value: 'speaker', sortable: true, filterable: true },
    { text: 'Data', value: 'date', sortable: true, filterable: true },
    { text: 'Desinscrever da sessão', value: 'actions', sortable: false, filterable: false },
  ];

  async mounted() {
    await this.$store.dispatch('loading');
    try {
      this.updateState();
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async updateState() {
    this.sessionsRegistered = [];
    let attendeeId = this.attendeeId();
    this.attendee = await RemoteServices.getAttendee(attendeeId);
    let sessions = await RemoteServices.getSessions();
    for (let i = 0; i < sessions.length; i++) {
      let session = sessions[i];
      if (session.participantsIds.includes(attendeeId)) {
        this.sessionsRegistered.push(session);
      }
    }
  }

  attendeeId() {
    return parseInt(this.$route.params.attendeeId);
  }

  async removeAttendeeFromSession(session: SessionDto) {
    await this.$store.dispatch('loading');
    try {
      let new_participantsIds = session.participantsIds
      new_participantsIds = new_participantsIds.filter((a) => a !== this.attendeeId());
      await RemoteServices.updateSession({"id": session.id, "subject": session.subject, "speaker": session.speaker, "date": session.date,
      "normalDishId": session.normalDishId, "vegetarianDishId": session.normalDishId, "participantsIds": new_participantsIds});
      this.updateState();
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async deleteAttendee() {
    await this.$store.dispatch('loading');
    try {
      await RemoteServices.deleteAttendee(this.attendee.id);
      this.$router.push('/attendees');
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

}

</script>
