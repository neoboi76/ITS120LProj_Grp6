import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HistoryModel } from '../models/history-model';
import { TokenStorageService } from './token-storage-service';
import { ContentType } from '../models/content-model';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Service class for history operations (storing, displaying, and manipulating stored verification records)

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  

  apiUrl: string = "http://localhost:8080/api/verification";//Backend url for verification operations

  constructor(
    private http: HttpClient,
    private tokenStorageService: TokenStorageService
  ) {}

  //Inserts JWT token to the header of every request
  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });
  }

  //Retrieves a list of all verifications of a user
  getHistory(id: number): Observable<HistoryModel[]> {

    return this.http.get<HistoryModel[]>(
      `${this.apiUrl}/user/${id.toString()}`,
      { headers: this.getHeaders() }
    );
  }

  //Submits a user verification request (TEXT)
  submitText(userId: number, contentType: ContentType, contentText: string): Observable<HistoryModel> {

    return this.http.post<HistoryModel>(
      `${this.apiUrl}/submit`, 
      { userId, contentType, contentText},
      { headers: this.getHeaders() }
    );
  }

  //Submits a user verification request (URL)
  submitLink(userId: number, contentType: ContentType, contentUrl: string): Observable<HistoryModel> {

    return this.http.post<HistoryModel>(
      `${this.apiUrl}/submit`, 
      { userId, contentType, contentUrl},
      { headers: this.getHeaders() }
    );


  }

  //Submits a user verification request (IMAGE)
  submitImage(userId: number, contentType: ContentType, imageBase64: string): Observable<HistoryModel> {

    return this.http.post<HistoryModel>(
      `${this.apiUrl}/submit`, 
      { userId, contentType, imageBase64},
      { headers: this.getHeaders() }
    );
  }

  //Retrieves a particular verification for a particular user
  getVerification(verificationId: number): Observable<HistoryModel> {

    return this.http.get<HistoryModel>(
      `${this.apiUrl}/result/${verificationId.toString()}`, 
      { headers: this.getHeaders() }
    );
  }


}
