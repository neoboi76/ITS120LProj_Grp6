import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NavComponent } from '../../components/nav/nav';
import { FooterComponent } from '../../components/footer/footer';
import { AdminService} from '../../services/admin-service';
import { UserResponse } from '../../models/admin-models';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Class that deals with user management operations by the admin

@Component({
    selector: 'app-admin-users',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, NavComponent, FooterComponent],
    templateUrl: './admin-users.html',
    styleUrls: ['./admin-users.css']
})
export class AdminUsersComponent implements OnInit {
    
    users: UserResponse[] = [];
    isLoading = true;
    errorMessage = '';
    successMessage = '';
    
    currentPage = 0;
    pageSize = 20;
    totalPages = 0;
    totalItems = 0;
    
    emailFilter = '';
    roleFilter = '';
    sortBy = 'userId';
    sortDirection = 'DESC';
    
    showDeleteModal = false;
    userToDelete: UserResponse | null = null;
    Math = Math;

    constructor(private adminService: AdminService) {}

    //Loads users on initialization
    ngOnInit(): void {
        this.loadUsers();
    }

    //Retrieves all existing users 
    loadUsers(): void {
        this.isLoading = true;
        this.errorMessage = '';
        
        this.adminService.getAllUsers(
            this.currentPage,
            this.pageSize,
            this.sortBy,
            this.sortDirection,
            this.emailFilter || undefined,
            this.roleFilter || undefined
        ).subscribe({
            next: (response: any) => {
                this.users = response.content;
                this.currentPage = response.currentPage;
                this.totalPages = response.totalPages;
                this.totalItems = response.totalItems;
                this.isLoading = false;
            },
            error: (err) => {
                console.error('Error loading users:', err);
                this.errorMessage = 'Failed to load users.';
                this.isLoading = false;
            }
        });
    }

    //Applies set filters
    applyFilters(): void {
        this.currentPage = 0;
        this.loadUsers();
    }

    //Removes filters
    clearFilters(): void {
        this.emailFilter = '';
        this.roleFilter = '';
        this.currentPage = 0;
        this.loadUsers();
    }

    //Goes to the next page (if applicable)
    changePage(page: number): void {
        if (page >= 0 && page < this.totalPages) {
            this.currentPage = page;
            this.loadUsers();
        }
    }

    //Changes sort criteria
    changeSort(column: string): void {
        if (this.sortBy === column) {
            this.sortDirection = this.sortDirection === 'ASC' ? 'DESC' : 'ASC';
        } else {
            this.sortBy = column;
            this.sortDirection = 'DESC';
        }
        this.loadUsers();
    }

    //Open delete popup warning
    openDeleteModal(user: UserResponse): void {
        this.userToDelete = user;
        this.showDeleteModal = true;
    }

    //Close delete popup warning
    closeDeleteModal(): void {
        this.showDeleteModal = false;
        this.userToDelete = null;
    }

    //Confirm delete operation
    confirmDelete(): void {
        if (this.userToDelete) {
            this.adminService.deleteUser(this.userToDelete.userId).subscribe({
                next: () => {
                    this.successMessage = `User ${this.userToDelete!.email} deleted successfully.`;
                    this.closeDeleteModal();
                    this.loadUsers();
                    setTimeout(() => this.successMessage = '', 3000);
                },
                error: (err) => {
                    this.errorMessage = 'Failed to delete user: ' + err.error;
                    this.closeDeleteModal();
                }
            });
        }
    }

    scrollToTop(): void {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    //Returns page numbers relative to the current page
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
}