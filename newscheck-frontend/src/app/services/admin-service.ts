import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from './token-storage-service';
import { AuditLogResponse, DashboardStats, PaginatedResponse, UserResponse, VerificationResponse } from '../models/admin-models';


@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private apiUrl = 'http://localhost:8080/admin';
  private auditLogUrl = 'http://localhost:8080/admin/audit-logs';

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

  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(
      `${this.apiUrl}/dashboard/stats`,
      { headers: this.getHeaders() }
    );
  }

  getAllUsers(
    page: number = 0,
    size: number = 20,
    sortBy: string = 'userId',
    sortDirection: string = 'DESC',
    email?: string,
    role?: string
  ): Observable<PaginatedResponse<UserResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    if (email) {
      params = params.set('email', email);
    }
    if (role) {
      params = params.set('role', role);
    }

    return this.http.get<PaginatedResponse<UserResponse>>(
      `${this.apiUrl}/users`,
      { headers: this.getHeaders(), params }
    );
  }

  getUserById(userId: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(
      `${this.apiUrl}/users/${userId}`,
      { headers: this.getHeaders() }
    );
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.delete(
      `${this.apiUrl}/users/${userId}`,
      { headers: this.getHeaders() }
    );
  }

  getAllVerifications(
    page: number = 0,
    size: number = 20,
    sortBy: string = 'submittedAt',
    sortDirection: string = 'DESC',
    userId?: number,
    status?: string,
    verdictType?: string
  ): Observable<PaginatedResponse<VerificationResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    if (userId) {
      params = params.set('userId', userId.toString());
    }
    if (status) {
      params = params.set('status', status);
    }
    if (verdictType) {
      params = params.set('verdictType', verdictType);
    }

    return this.http.get<PaginatedResponse<VerificationResponse>>(
      `${this.apiUrl}/verifications`,
      { headers: this.getHeaders(), params }
    );
  }

  getVerificationById(verificationId: number): Observable<VerificationResponse> {
    return this.http.get<VerificationResponse>(
      `${this.apiUrl}/verifications/${verificationId}`,
      { headers: this.getHeaders() }
    );
  }

  deleteVerification(verificationId: number): Observable<any> {
    return this.http.delete(
      `${this.apiUrl}/verifications/${verificationId}`,
      { headers: this.getHeaders() }
    );
  }

  getAllAuditLogs(
    page: number = 0,
    size: number = 20,
    sortBy: string = 'timestamp',
    sortDirection: string = 'DESC',
    userId?: number,
    action?: string,
    startDate?: string,
    endDate?: string
  ): Observable<PaginatedResponse<AuditLogResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    if (userId) {
      params = params.set('userId', userId.toString());
    }
    if (action) {
      params = params.set('action', action);
    }
    if (startDate) {
      params = params.set('startDate', startDate);
    }
    if (endDate) {
      params = params.set('endDate', endDate);
    }

    return this.http.get<PaginatedResponse<AuditLogResponse>>(
      this.auditLogUrl,
      { headers: this.getHeaders(), params }
    );
  }

  getUserAuditLogs(
    userId: number,
    page: number = 0,
    size: number = 20,
    sortBy: string = 'timestamp',
    sortDirection: string = 'DESC'
  ): Observable<PaginatedResponse<AuditLogResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    return this.http.get<PaginatedResponse<AuditLogResponse>>(
      `${this.auditLogUrl}/user/${userId}`,
      { headers: this.getHeaders(), params }
    );
  }

  getVerificationAuditLogs(
    verificationId: number,
    page: number = 0,
    size: number = 20,
    sortBy: string = 'timestamp',
    sortDirection: string = 'DESC'
  ): Observable<PaginatedResponse<AuditLogResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    return this.http.get<PaginatedResponse<AuditLogResponse>>(
      `${this.auditLogUrl}/verification/${verificationId}`,
      { headers: this.getHeaders(), params }
    );
  }

  getAuditLogStats(): Observable<any> {
    return this.http.get(
      `${this.auditLogUrl}/stats`,
      { headers: this.getHeaders() }
    );
  }

  cleanupOldAuditLogs(daysOld: number = 90): Observable<any> {
    let params = new HttpParams().set('daysOld', daysOld.toString());
    
    return this.http.delete(
      `${this.auditLogUrl}/cleanup`,
      { headers: this.getHeaders(), params }
    );
  }
}