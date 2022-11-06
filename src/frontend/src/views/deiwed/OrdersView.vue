<template>
  <v-card :flat="$store.getters.isMobile" class="table pb-4">
    <v-card-title>Pedidos para os almoços do DEI às Quartas</v-card-title>
    <v-card-text>
      <v-expansion-panels v-model="panel">
        <v-expansion-panel v-model="panel">
          <v-expansion-panel-header>Adicionar novo pedido manualmente</v-expansion-panel-header>
          <v-expansion-panel-content>
            <v-form>
              <v-container>
                <v-row>
                  <v-col cols="12" md="3">
                    <v-text-field v-model="new_date" :rules="dateRules" label="Data" required></v-text-field>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-select v-model="new_normalOptionName" :items="items_normalOptionsNames" :rules="optionRules" label="Prato normal" required></v-select>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-select v-model="new_vegetarianOptionName" :items="items_vegetarianOptionsNames" :rules="optionRules" label="Prato vegetariano" required></v-select>
                  </v-col>
                  <v-col cols="12" md="1" style="text-align: left">
                    <v-btn @click='createOrder()' class="mx-2" fab dark small color="blue"><v-icon dark>mdi-plus</v-icon></v-btn>
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
        :items="orders"
        :search="search"
        :loading="loading"
        single-select
        locale="pt-PT"
        no-data-text="Não existem pedidos registados"
        no-results-text="Nenhum pedido corresponde aos critérios indicados"
        sort-by="name"
      >
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
import DishDto from '@/models/deiwed/DishDto';
import OrderDto from '@/models/deiwed/OrderDto';
import RemoteServices from '@/services/RemoteServices';
import { Component, Vue } from 'vue-property-decorator';
import { DataTableHeader } from 'vuetify';
import '@mdi/font/css/materialdesignicons.css';

@Component
export default class OrdersView extends Vue {

  loading = true;
  panel = false;

  dateRules = [
    (v: string) => !!v || 'É obrigatório indicar uma data',
    (v: string) => /^\d{4}-\d{2}-\d{2}$/.test(v) || 'A data deve ter o formato YYYY-MM-DD'];
  optionRules = [(v: string) => !!v || 'É obrigatório indicar um prato'];

  new_date = '';
  items_normalOptions: Array<DishDto> = [];
  items_normalOptionsNames: Array<string> = [];
  new_normalOptionName = '';
  items_vegetarianOptions: Array<DishDto> = [];
  items_vegetarianOptionsNames: Array<string> = [];
  new_vegetarianOptionName = '';

  search = '';
  orders: OrderDto[] = [];
  headers: DataTableHeader[] = [
    { text: 'Data', value: 'date', sortable: true, filterable: true },
    { text: 'Prato normal', value: 'normalOption.name', sortable: true, filterable: true },
    { text: 'Prato vegetariano', value: 'vegetarianOption.name', sortable: true, filterable: true },
  ];

  async mounted() {
    await this.$store.dispatch('loading');
    try {
      this.items_normalOptions = await RemoteServices.getNormalDishes();
      this.items_normalOptionsNames = this.items_normalOptions.map((d) => d.name);
      this.items_vegetarianOptions = await RemoteServices.getVegetarianDishes();
      this.items_vegetarianOptionsNames = this.items_vegetarianOptions.map((d) => d.name);
      this.orders = await RemoteServices.getOrders();
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async createOrder() {
    if (this.new_date === '' || this.new_normalOptionName === '' || this.new_vegetarianOptionName === '') {
      this.$store.dispatch('error', 'Por favor preencha todos os campos'); return; }
    if (!/^\d{4}-\d{2}-\d{2}$/.test(this.new_date)) {
      this.$store.dispatch('error', 'A data deve ter o formato YYYY-MM-DD'); return; }
    await this.$store.dispatch('loading');
    this.panel = false;
    try {
      let new_normalOptionId = 0; let new_vegetarianOptionId = 0;
      for (const d of this.items_normalOptions) {if (d.name === this.new_normalOptionName) {new_normalOptionId = d.id; break;}}
      for (const d of this.items_vegetarianOptions) {if (d.name === this.new_vegetarianOptionName) {new_vegetarianOptionId = d.id; break;}}
      await RemoteServices.createOrder(this.new_date, {"normalOptionDishId": new_normalOptionId, "vegetarianOptionDishId": new_vegetarianOptionId});
      this.orders = await RemoteServices.getOrders();
      this.loading = false;
      this.new_date = '';
      this.new_normalOptionName = '';
      this.new_vegetarianOptionName = '';
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
}

</script>