import { User } from './user';
import { DayOfWeek } from './dayOfWeek';

export class Timesheet {
  id: number;
  weekEndingDate: string;
  submitted: boolean;
  dayOfWeeks: DayOfWeek[];
}


