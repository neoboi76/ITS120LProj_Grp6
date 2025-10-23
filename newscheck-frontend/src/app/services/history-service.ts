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

  getHistory(id: number): Observable<HistoryModel[]> {

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });

    return this.http.get<HistoryModel[]>(this.apiUrl + "/user/" + id.toString(), 
      { headers }
    );
  }

  submitText(userId: number, contentType: ContentType, contentText: string): Observable<HistoryModel> {

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });

    return this.http.post<HistoryModel>(this.apiUrl + "/submit", 
      { userId, contentType, contentText},
      { headers }
    );
  }

  submitLink(userId: number, contentType: ContentType, contentUrl: string): Observable<HistoryModel> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });

    return this.http.post<HistoryModel>(this.apiUrl + "/submit", 
      { userId, contentType, contentUrl},
      { headers }
    );


  }

  submitImage(userId: number, contentType: ContentType, imageBase64: string): Observable<HistoryModel> {
    
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });

    return this.http.post<HistoryModel>(this.apiUrl + "/submit", 
      { userId, contentType, imageBase64},
      { headers }
    );
  }

  getVerification(verificationId: number): Observable<HistoryModel> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });

    return this.http.get<HistoryModel>(this.apiUrl + '/result/' + verificationId.toString(), 
      { headers }
    )
  }

}
