export default class SessionDto {
  id: number = 0;
  subject: string = '';
  speaker: string = '';
  date: string = '';
  normalDishId: number = 0;
  vegetarianDishId: number = 0;
  participantsIds: number[] = [];
}
