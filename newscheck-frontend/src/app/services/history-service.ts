import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HistoryModel } from '../models/history-model';
import { TokenStorageService } from './token-storage-service';

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  

  apiUrl: string = "http://localhost:8080/api/verification/user/";

  constructor(
    private http: HttpClient,
    private tokenStorageService: TokenStorageService
  ) {}

  getHistory(id: number): Observable<HistoryModel> {

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });

    return this.http.get<HistoryModel>(this.apiUrl + id.toString(), 
      { headers }
    );
  }

}
