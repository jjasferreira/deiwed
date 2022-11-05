import DishDto from "./DishDto";

export default class OrderDto {
    date: string = '';
    normalOption: DishDto = new DishDto();
    vegetarianOption: DishDto = new DishDto();
}