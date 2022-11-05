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
                    <v-select v-model="new_normalOption" :items="items_normalOptions.name" :rules="optionRules" label="Prato normal" required></v-select>
                  </v-col>
                  <v-col cols="12" md="2">
                    <v-select v-model="new_vegetarianOption" :items="items_VegetarianOptions.name" :rules="optionRules" label="Prato vegetariano" required></v-select>
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
    (v: string) => /^\d{4}-\d{2}-\d{2}$/.test(v) || 'A data deve ter o formato YYYY-MM-DD',
  ];
  optionRules = [
    (v: string) => !!v || 'É obrigatório indicar um prato',
  ];

  new_date = '';
  new_normalOption = new DishDto();
  new_vegetarianOption = new DishDto();
  items_normalOptions: Array<DishDto[]> = [];
  items_vegetarianOptions: Array<DishDto> = [];

  search = '';
  orders: OrderDto[] = [];
  headers: DataTableHeader[] = [
    { text: 'Data', value: 'date', sortable: true, filterable: true },
    { text: 'Prato normal', value: 'normalOption.name', sortable: true, filterable: true },
    { text: 'Prato vegetariano', value: 'vegetarianOption.name', sortable: true, filterable: true },
  ];

  async mounted() {
    this.items_normalOptions = RemoteServices.getNormalDishes();
    this.items_vegetarianOptions = RemoteServices.getVegetarianDishes();
    await this.$store.dispatch('loading');
    try {
      this.orders = await RemoteServices.getOrders();
      this.loading = false;
    } catch (error) {
      this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  async createOrder() {
    if (this.new_date === '' || this.new_normalOption === '' || this.new_vegetarianOption === '') {
      this.$store.dispatch('error', 'Por favor preencha todos os campos');
      return;
    }
    const order = new OrderDto();
    order.date = this.new_date;
    order.normalOption = this.new_normalOption;
    order.vegetarianOption = this.new_vegetarianOption;
    try {
      await RemoteServices.createOrder(order);
      this.orders.push(order);
      this.$store.dispatch('success', 'Pedido criado com sucesso');
    } catch (error) {
      this.$store.dispatch('error', error);
    }
  }
}

</script>