export default class AttendeeDto {
  id: number = 0;
  poster: undefined;
  subject: string = '';
  speaker: string = '';
  date: string = '';
  normalDish: string = '';
  vegetarianDish: string = '';
  participants: Array<string> = [];
}
