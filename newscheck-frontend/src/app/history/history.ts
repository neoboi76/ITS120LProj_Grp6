import { Component, OnInit } from '@angular/core';
import { NavComponent } from "../components/nav/nav";
import { FooterComponent } from "../components/footer/footer";
import { FormsModule } from '@angular/forms';
import { HistoryModel } from '../models/history-model';
import { HistoryService } from '../services/history-service';
import { TokenStorageService } from '../services/token-storage-service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-history',
  imports: [NavComponent, FooterComponent, FormsModule, DatePipe],
  templateUrl: './history.html',
  styleUrl: './history.css'
})
export class HistoryPageComponent implements OnInit {


  constructor(
    private historyService: HistoryService,
    private tokenStorageService: TokenStorageService
  ) 
  {}

  history: any;

  ngOnInit(): void {
    this.historyService.getHistory(this.tokenStorageService.getUsrId()).subscribe({
      next: (data) => {
      this.history = data.map((item: any) => ({
        verificationId: item.verificationId,
        status: item.status,
        verdict: item.verdictType ?? '—',
        reasoning: item.reasoning,
        confidenceScore: item.confidenceScore,
        evidences: item.evidences,
        date: item.submittedAt,
        news: item.claim
      }));
      console.log(data);
      console.log("Mapped history:", this.history);
    },
      error: (err) => {
        console.log(err);
      }
    })
  }


  searchQuery = '';

  filteredHistory() {
    const query = this.searchQuery.toLowerCase().trim();
    if (!query) return this.history;
    return this.history.filter((item: {claim: string; date: string; verdict: string; status: string; }) =>
      item.claim.toLowerCase().includes(query) ||
      item.date.toLowerCase().includes(query) ||
      item.verdict.toLowerCase().includes(query) ||
      item.status.toLowerCase().includes(query)
    );
  }



}
