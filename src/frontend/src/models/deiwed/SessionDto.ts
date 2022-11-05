import AttendeeDto from "./AttendeeDto";
import DishDto from "./DishDto";

export default class SessionDto {
  id: number = 0;
  poster: undefined;
  subject: string = '';
  speaker: string = '';
  date: string = '';
  normalDish: DishDto = new DishDto();
  vegetarianDish: DishDto = new DishDto();
  participants: Array<AttendeeDto> = [];
}
