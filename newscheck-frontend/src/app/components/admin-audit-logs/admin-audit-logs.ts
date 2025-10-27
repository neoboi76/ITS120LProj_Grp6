import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NavComponent } from '../../components/nav/nav';
import { FooterComponent } from '../../components/footer/footer';
import { AdminService } from '../../services/admin-service';
import { AuditLogResponse } from '../../models/admin-models';

@Component({
    selector: 'app-admin-audit-logs',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, NavComponent, FooterComponent],
    templateUrl: './admin-audit-logs.html',
    styleUrls: ['./admin-audit-logs.css']
})
export class AdminAuditLogsComponent implements OnInit {
    
    auditLogs: AuditLogResponse[] = [];
    isLoading = true;
    errorMessage = '';
    successMessage = '';
    
    currentPage = 0;
    pageSize = 20;
    totalPages = 0;
    totalItems = 0;
    
    userIdFilter: number | null = null;
    actionFilter = '';
    startDateFilter = '';
    endDateFilter = '';
    sortBy = 'timestamp';
    sortDirection = 'DESC';
    
    stats: any = null;
    Math = Math;

    constructor(private adminService: AdminService) {}

    ngOnInit(): void {
        this.loadAuditLogs();
        this.loadStats();
    }

    loadAuditLogs(): void {
        this.isLoading = true;
        this.errorMessage = '';
        
        this.adminService.getAllAuditLogs(
            this.currentPage,
            this.pageSize,
            this.sortBy,
            this.sortDirection,
            this.userIdFilter || undefined,
            this.actionFilter || undefined,
            this.startDateFilter || undefined,
            this.endDateFilter || undefined
        ).subscribe({
            next: (response: any) => {
                this.auditLogs = response.content;
                this.currentPage = response.currentPage;
                this.totalPages = response.totalPages;
                this.totalItems = response.totalItems;
                this.isLoading = false;
            },
            error: (err) => {
                console.error('Error loading audit logs:', err);
                this.errorMessage = 'Failed to load audit logs.';
                this.isLoading = false;
            }
        });
    }

    loadStats(): void {
        this.adminService.getAuditLogStats().subscribe({
            next: (data) => {
                this.stats = data;
            },
            error: (err) => {
                console.error('Error loading stats:', err);
            }
        });
    }

    applyFilters(): void {
        this.currentPage = 0;
        this.loadAuditLogs();
    }

    clearFilters(): void {
        this.userIdFilter = null;
        this.actionFilter = '';
        this.startDateFilter = '';
        this.endDateFilter = '';
        this.currentPage = 0;
        this.loadAuditLogs();
    }

    changePage(page: number): void {
        if (page >= 0 && page < this.totalPages) {
            this.currentPage = page;
            this.loadAuditLogs();
        }
    }

    changeSort(column: string): void {
        if (this.sortBy === column) {
            this.sortDirection = this.sortDirection === 'ASC' ? 'DESC' : 'ASC';
        } else {
            this.sortBy = column;
            this.sortDirection = 'DESC';
        }
        this.loadAuditLogs();
    }

    cleanupOldLogs(daysOld: number = 90): void {
        if (confirm(`Are you sure you want to delete all audit logs older than ${daysOld} days? This cannot be undone.`)) {
            this.adminService.cleanupOldAuditLogs(daysOld).subscribe({
                next: (response: any) => {
                    this.successMessage = `Successfully deleted ${response.deletedCount} old audit logs.`;
                    this.loadAuditLogs();
                    this.loadStats();
                    setTimeout(() => this.successMessage = '', 5000);
                },
                error: (err) => {
                    this.errorMessage = 'Failed to cleanup logs: ' + err.error;
                }
            });
        }
    }

    scrollToTop(): void {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    getPageNumbers(): number[] {
        const maxVisible = 5;
        const pages: number[] = [];
        let start = Math.max(0, this.currentPage - Math.floor(maxVisible / 2));
        let end = Math.min(this.totalPages, start + maxVisible);
        
        if (end - start < maxVisible) {
            start = Math.max(0, end - maxVisible);
        }
        
        for (let i = start; i < end; i++) {
            pages.push(i);
        }
        return pages;
    }

    getActionColor(action: string): string {
        const actionColors: {[key: string]: string} = {
            'USER_LOGIN': 'bg-blue-500/30 text-blue-300',
            'USER_LOGOUT': 'bg-gray-500/30 text-gray-300',
            'USER_REGISTER': 'bg-green-500/30 text-green-300',
            'PASSWORD_RESET': 'bg-yellow-500/30 text-yellow-300',
            'LOGIN_FAILED': 'bg-red-500/30 text-red-300',
            'VERIFICATION_SUBMITTED': 'bg-purple-500/30 text-purple-300',
            'VERIFICATION_COMPLETED': 'bg-green-500/30 text-green-300',
            'VERIFICATION_FAILED': 'bg-red-500/30 text-red-300',
            'VERIFICATION_VIEWED': 'bg-cyan-500/30 text-cyan-300',
            'USER_PROFILE_UPDATED': 'bg-blue-500/30 text-blue-300',
            'SYSTEM_ERROR': 'bg-red-500/30 text-red-300'
        };
        return actionColors[action] || 'bg-gray-500/30 text-gray-300';
    }

    truncateDetails(details: string, maxLength: number = 60): string {
        if (!details) return 'N/A';
        return details.length > maxLength ? details.substring(0, maxLength) + '...' : details;
    }
}