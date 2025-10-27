import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NavComponent } from '../../components/nav/nav';
import { FooterComponent } from '../../components/footer/footer';
import { AdminService, UserResponse } from '../../services/admin-service';

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
  
  // Pagination
  currentPage = 0;
  pageSize = 20;
  totalPages = 0;
  totalItems = 0;
  
  // Filters
  emailFilter = '';
  roleFilter = '';
  sortBy = 'userId';
  sortDirection = 'DESC';
  
  // Delete confirmation
  showDeleteModal = false;
  userToDelete: UserResponse | null = null;
Math: any;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

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

  applyFilters(): void {
    this.currentPage = 0;
    this.loadUsers();
  }

  clearFilters(): void {
    this.emailFilter = '';
    this.roleFilter = '';
    this.currentPage = 0;
    this.loadUsers();
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadUsers();
    }
  }

  changeSort(column: string): void {
    if (this.sortBy === column) {
      this.sortDirection = this.sortDirection === 'ASC' ? 'DESC' : 'ASC';
    } else {
      this.sortBy = column;
      this.sortDirection = 'DESC';
    }
    this.loadUsers();
  }

  openDeleteModal(user: UserResponse): void {
    this.userToDelete = user;
    this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    this.showDeleteModal = false;
    this.userToDelete = null;
  }

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