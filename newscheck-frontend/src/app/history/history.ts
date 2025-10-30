import { Component, OnInit } from '@angular/core';
import { NavComponent } from "../components/nav/nav";
import { FooterComponent } from "../components/footer/footer";
import { FormsModule } from '@angular/forms';
import { HistoryModel } from '../models/history-model';
import { HistoryService } from '../services/history-service';
import { TokenStorageService } from '../services/token-storage-service';
import { DatePipe } from '@angular/common';
import { RouterLink } from "@angular/router";

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Service class for history page 

@Component({
  selector: 'app-history',
  imports: [NavComponent, FooterComponent, FormsModule, DatePipe, RouterLink],
  templateUrl: './history.html',
  styleUrl: './history.css'
})
export class HistoryPageComponent implements OnInit {


  constructor(
    private historyService: HistoryService,
    private tokenStorageService: TokenStorageService
  ) 
  {}
  
  isLoading = true;

  history: HistoryModel[] = [];

  //Retrieves all verifications associated with logged user
  ngOnInit(): void {
    this.historyService.getHistory(this.tokenStorageService.getUsrId()).subscribe({
      next: (data) => {
      this.history = data.map((item: HistoryModel) => ({
        verificationId: item.verificationId ?? 0,
        status: item.status ?? 'N/A',
        verdictType: item.verdictType ?? 'N/A',
        reasoning: item.reasoning ?? 'N/A',
        confidenceScore: item.confidenceScore ?? 0,
        evidences: item.evidences ?? null,
        date: item.submittedAt ?? 'N/A',
        claim: item.claim ?? 'N/A',
        submittedAt: item.submittedAt ?? 'N/A',
        completedAt: item.completedAt ?? 'N/A',
        message: item.message ?? 'N/A'
      }));
      this.isLoading = false;
      console.log("Mapped history:", this.history);
    },
      error: (err) => {
        console.log(err);
      }
    })
  }


  searchQuery = '';

  //Method that returns verifications associated with inputted search query
  filteredHistory() {
    const query = this.searchQuery.toLowerCase().trim();
    if (!query) return this.history;
    return this.history.filter((item: HistoryModel) =>
      (item.claim ?? '').toLowerCase().includes(query) ||
      (item.date ?? '').toLowerCase().includes(query) ||
      (item.verdictType ?? '').toLowerCase().includes(query) ||
      (item.status ?? '').toLowerCase().includes(query)
    );
  }

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

}
