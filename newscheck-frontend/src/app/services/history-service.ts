import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HistoryModel } from '../models/history-model';
import { TokenStorageService } from './token-storage-service';
import { ContentType } from '../models/content-model';

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  

  apiUrl: string = "http://localhost:8080/api/verification";

  constructor(
    private http: HttpClient,
    private tokenStorageService: TokenStorageService
  ) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });
  }

  getHistory(id: number): Observable<HistoryModel[]> {

    return this.http.get<HistoryModel[]>(
      `${this.apiUrl}/user/${id.toString()}`,
      { headers: this.getHeaders() }
    );
  }

  submitText(userId: number, contentType: ContentType, contentText: string): Observable<HistoryModel> {

    return this.http.post<HistoryModel>(
      `${this.apiUrl}/submit`, 
      { userId, contentType, contentText},
      { headers: this.getHeaders() }
    );
  }

  submitLink(userId: number, contentType: ContentType, contentUrl: string): Observable<HistoryModel> {

    return this.http.post<HistoryModel>(
      `${this.apiUrl}/submit`, 
      { userId, contentType, contentUrl},
      { headers: this.getHeaders() }
    );


  }

  submitImage(userId: number, contentType: ContentType, imageBase64: string): Observable<HistoryModel> {

    return this.http.post<HistoryModel>(
      `${this.apiUrl}/submit`, 
      { userId, contentType, imageBase64},
      { headers: this.getHeaders() }
    );
  }

  getVerification(verificationId: number): Observable<HistoryModel> {

    return this.http.get<HistoryModel>(
      `${this.apiUrl}/result/${verificationId.toString()}`, 
      { headers: this.getHeaders() }
    );
  }


}
