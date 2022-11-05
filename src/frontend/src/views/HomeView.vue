<template>
  <div>
    <div style="height:100vh position:relative overflow:hidden">
      <div style="position:absolute;height:100vh;width:100vw;">
        <v-img src="@/assets/img/tecnico_background.png" height="100vh" aspect-ratio="1.7" cover></v-img>
      </div>
    </div>

    <v-toolbar prominent dark height='100px' src="@/assets/img/background.png">
      <v-toolbar-title class='mx-auto mb-5' elevation="3" > Bem-vindo ao sistema de gestão dos eventos {{ appName }} </v-toolbar-title>
    </v-toolbar>

    <v-row class="d-flex justify-center mt-0">
      <v-col cols="12" md="4">
        <v-card class='mx-auto' height='500px'>
          <v-img @click='goTo(sessions)' src='https://images.unsplash.com/photo-1524178232363-1fb2b075b655?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80' height='300px'></v-img>
          <v-card-title class="ml-2"> Sessões </v-card-title>
          <v-card-subtitle class="ml-2 mt-1" style="text-align:left"> Veja a lista de sessões agendadas do DEIWed com os respetivos temas, oradores, datas, pratos para o almoço e participantes. Adicione, remova ou edite as sessões, conforme for necessário. </v-card-subtitle>
          <v-card-actions> <v-btn @click='goTo(sessions)' class="mt-n1 ml-1" color='brown' text> Consultar </v-btn> </v-card-actions>
        </v-card>
      </v-col>
        <v-col cols="12" md="4">
          <v-row rows="2" md="1">
            <v-card class='mt-3 mx-auto' height='250px'>
              <v-img @click='goTo(attendees)' src='https://images.unsplash.com/photo-1556761175-b413da4baf72?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1074&q=80' height='150px'></v-img>
              <v-card-title class="ml-2"> Participantes </v-card-title>
              <v-card-actions> <v-btn @click='goTo(attendees)' class="mt-n5 ml-1" color='blue darken-1' text> Consultar </v-btn> </v-card-actions>
            </v-card>
          </v-row>
          <v-row rows="2" md="1">
              <v-card class='mt-3 mx-auto' height='238px' width="250px">
                <v-img @click='goTo(orders)' src='https://images.unsplash.com/photo-1623475173140-ad2f0369ca92?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=724&q=80' height='140px'></v-img>
                <v-card-title class="ml-2"> Pedidos </v-card-title>
                <v-card-actions> <v-btn @click='goTo(orders)' class="mt-n5 ml-1" color='orange' text> Consultar </v-btn> </v-card-actions>
              </v-card>
              <v-card class='mt-3 ml-3 mx-auto' height='238px' width="250px">
                <v-img @click='goTo(dishes)' src='https://images.unsplash.com/photo-1504674900247-0877df9cc836?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80' height='140px'></v-img>
                <v-card-title class="ml-2"> Pratos </v-card-title>
                <v-card-actions> <v-btn @click='goTo(dishes)' class="mt-n5 ml-1" color='green' text> Consultar </v-btn> </v-card-actions>
              </v-card>
          </v-row>
        </v-col>
    </v-row>

    
  </div>
</template>

<script lang='ts'>
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class HomeView extends Vue {

  attendees = '/attendees';
  dishes = '/dishes';
  orders = '/orders';
  sessions = '/sessions';

  goTo(path: string) {
    if (this.$route.path == path) {
      this.$router.go(0);
    } else {
      this.$router.push(path);
    }
  }

  appName: string = process.env.VUE_APP_NAME;
}
</script>

<style> body {background-image: url('@/assets/img/tecnico_background.png');} </style>