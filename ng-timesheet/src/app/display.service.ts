import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Timesheet } from 'src/models/timesheet';

@Injectable({
  providedIn: 'root'
})
export class DisplayService {

  private url = 'http://localhost:8080';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  getAllTimesheetsByUserId(userId: number): Observable<Timesheet[]> {
    return this.http.get<Timesheet[]>(`${this.url}/timesheet/user/id/${userId}`).pipe();
  }

  saveTimesheet(timesheet: Timesheet, userId: number): Observable<any> {
    return this.http.put(`${this.url}/timesheet/user/id/${userId}`, timesheet, this.httpOptions).pipe();
  }
}
