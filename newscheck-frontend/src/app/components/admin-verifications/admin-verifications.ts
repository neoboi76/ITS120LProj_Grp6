import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NavComponent } from '../../components/nav/nav';
import { FooterComponent } from '../../components/footer/footer';
import { AdminService} from '../../services/admin-service';
import { VerificationResponse } from '../../models/admin-models';

@Component({
    selector: 'app-admin-verifications',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, NavComponent, FooterComponent],
    templateUrl: './admin-verifications.html',
    styleUrls: ['./admin-verifications.css']
})
export class AdminVerificationsComponent implements OnInit {
    
    verifications: VerificationResponse[] = [];
    isLoading = true;
    errorMessage = '';
    successMessage = '';
    
    currentPage = 0;
    pageSize = 20;
    totalPages = 0;
    totalItems = 0;
    
    userIdFilter: number | null = null;
    statusFilter = '';
    verdictTypeFilter = '';
    sortBy = 'submittedAt';
    sortDirection = 'DESC';
    
    showDeleteModal = false;
    verificationToDelete: VerificationResponse | null = null;
    Math = Math;

    constructor(private adminService: AdminService) {}

    ngOnInit(): void {
        this.loadVerifications();
    }

    loadVerifications(): void {
        this.isLoading = true;
        this.errorMessage = '';
        
        this.adminService.getAllVerifications(
            this.currentPage,
            this.pageSize,
            this.sortBy,
            this.sortDirection,
            this.userIdFilter || undefined,
            this.statusFilter || undefined,
            this.verdictTypeFilter || undefined
        ).subscribe({
            next: (response: any) => {
                this.verifications = response.content;
                this.currentPage = response.currentPage;
                this.totalPages = response.totalPages;
                this.totalItems = response.totalItems;
                this.isLoading = false;
            },
            error: (err) => {
                console.error('Error loading verifications:', err);
                this.errorMessage = 'Failed to load verifications.';
                this.isLoading = false;
            }
        });
    }

    applyFilters(): void {
        this.currentPage = 0;
        this.loadVerifications();
    }

    clearFilters(): void {
        this.userIdFilter = null;
        this.statusFilter = '';
        this.verdictTypeFilter = '';
        this.currentPage = 0;
        this.loadVerifications();
    }

    changePage(page: number): void {
        if (page >= 0 && page < this.totalPages) {
            this.currentPage = page;
            this.loadVerifications();
        }
    }

    changeSort(column: string): void {
        if (this.sortBy === column) {
            this.sortDirection = this.sortDirection === 'ASC' ? 'DESC' : 'ASC';
        } else {
            this.sortBy = column;
            this.sortDirection = 'DESC';
        }
        this.loadVerifications();
    }

    openDeleteModal(verification: VerificationResponse): void {
        this.verificationToDelete = verification;
        this.showDeleteModal = true;
    }

    closeDeleteModal(): void {
        this.showDeleteModal = false;
        this.verificationToDelete = null;
    }

    confirmDelete(): void {
        if (this.verificationToDelete) {
            this.adminService.deleteVerification(this.verificationToDelete.verificationId).subscribe({
                next: () => {
                    this.successMessage = `Verification #${this.verificationToDelete!.verificationId} deleted successfully.`;
                    this.closeDeleteModal();
                    this.loadVerifications();
                    setTimeout(() => this.successMessage = '', 3000);
                },
                error: (err) => {
                    this.errorMessage = 'Failed to delete verification: ' + err.error;
                    this.closeDeleteModal();
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

    truncateClaim(claim: string, maxLength: number = 50): string {
        if (!claim) return 'N/A';
        return claim.length > maxLength ? claim.substring(0, maxLength) + '...' : claim;
    }
}