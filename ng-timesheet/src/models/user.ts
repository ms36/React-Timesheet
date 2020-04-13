import { Timesheet } from './timesheet';

export class User {
  id: number;
  userName: string;
  password: string;
  firstName: string;
  lastName: string;
  timesheets: Timesheet[];
}
