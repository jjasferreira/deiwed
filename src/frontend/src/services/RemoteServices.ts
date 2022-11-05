import AttendeeDto from '@/models/deiwed/AttendeeDto';
import DishDto from '@/models/deiwed/DishDto';
import OrderDto from '@/models/deiwed/OrderDto';
import SessionDto from '@/models/deiwed/SessionDto';
import DeiwedError from '@/models/error/DeiwedError';
import axios from 'axios';

const httpClient = axios.create();
httpClient.defaults.timeout = 50000;
httpClient.defaults.baseURL = process.env.VUE_APP_ROOT_API;
httpClient.defaults.headers.post['Content-Type'] = 'application/json';

const token = 'ist199259';

export default class RemoteServices {

  static async getAttendees(): Promise<AttendeeDto[]> {
    return httpClient
      .get('/attendees')
      .then((response) => response.data)
      .catch(async (error) => {
        throw new DeiwedError(
          await this.errorMessage(error),
          error.response.data.code
        );
      });
  }

  static async createAttendee(attendee: AttendeeDto): Promise<AttendeeDto> {
    return httpClient
      .post('/attendees', attendee)
      .then((response) => response.data)
      .catch(async (error) => {
        throw new DeiwedError(
          await this.errorMessage(error),
          error.response.data.code
        );
      });
  }

  static async editAttendee(attendee: AttendeeDto): Promise<AttendeeDto> {
    return httpClient
      .put(`/attendees/${attendee.id}`, attendee)
      .then((response) => response.data)
      .catch(async (error) => {
        throw new DeiwedError(
          await this.errorMessage(error),
          error.response.data.code
        );
      });
  }

  static async deleteAttendee(attendee: AttendeeDto): Promise<void> {
    return httpClient
      .delete(`/attendees/${attendee.id}`)
      .then((response) => response.data)
      .catch(async (error) => {
        throw new DeiwedError(
          await this.errorMessage(error),
          error.response.data.code
        );
      });
  }

  static async getDishes(): Promise<DishDto[]> {
    return axios
      .get('https://eindhoven.rnl.tecnico.ulisboa.pt/food-store/api/v1/dishes')
      .then((response) => response.data)
      .catch(async (error) => {
        throw new DeiwedError(
          await this.errorMessage(error),
          error.response.data.code
        );
      });
  }

  static async getNormalDishes(): Promise<DishDto[]> {
    return RemoteServices.getDishes().then((dishes) => {
        return dishes.filter((dish) => !dish.vegetarian).sort((a, b) => a.name.localeCompare(b.name));
    });
  };
  static async getNormalDishesNames(): Promise<string[]> {
    return this.getNormalDishes().then((dishes) => {
        return dishes.map((dish) => dish.name);
    });
  };
  static async getVegetarianDishes(): Promise<DishDto[]> {
    return RemoteServices.getDishes().then((dishes) => {
        return dishes.filter((dish) => dish.vegetarian).sort((a, b) => a.name.localeCompare(b.name));
    });
  };
  static async getVegetarianDishesNames(): Promise<string[]> {
    return this.getVegetarianDishes().then((dishes) => {
        return dishes.map((dish) => dish.name);
    });
  };

  static async getOrders(): Promise<OrderDto[]> {
    return axios
      .get('https://eindhoven.rnl.tecnico.ulisboa.pt/food-store/api/v1/orders')
      .then((response) => response.data)
      .catch(async (error) => {
        throw new DeiwedError(
          await this.errorMessage(error),
          error.response.data.code
        );
      });
  }

  static async createOrder(order: OrderDto): Promise<OrderDto[]> {
    return axios
      .create({baseURL: 'https://eindhoven.rnl.tecnico.ulisboa.pt/food-store/api/v1',
              headers: {'Authorization': 'Bearer ' + token}})
      .post('/orders/' + order.date, order)
      .then((response) => response.data)
      .catch(async (error) => {
        throw new DeiwedError(
          await this.errorMessage(error),
          error.response.data.code
        );
      });
  }

  static async getSessions(): Promise<SessionDto[]> {
    return httpClient
      .get('/sessions')
      .then((response) => response.data)
      .catch(async (error) => {
        throw new DeiwedError(
          await this.errorMessage(error),
          error.response.data.code
        );
      });
  }

  static async createSession(session: SessionDto): Promise<SessionDto> {
    return httpClient
      .post('/sessions', session)
      .then((response) => response.data)
      .catch(async (error) => {
        throw new DeiwedError(
          await this.errorMessage(error),
          error.response.data.code
        );
      });
  }

  static async errorMessage(error: any): Promise<string> {
    if (error.message === 'Network Error') {
      return 'Unable to connect to server';
    } else if (error.message.split(' ')[0] === 'timeout') {
      return 'Request timeout - Server took too long to respond';
    } else if (error.response?.data?.message) {
      return error.response.data.message;
    } else {
      return 'Unknown Error - Contact admin';
    }
  }
}
