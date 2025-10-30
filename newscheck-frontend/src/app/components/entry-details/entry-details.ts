import { Component } from '@angular/core';
import { NavComponent } from "../nav/nav";
import { FooterComponent } from "../footer/footer";
import { HistoryModel } from '../../models/history-model';
import { HistoryService } from '../../services/history-service';
import { TokenStorageService } from '../../services/token-storage-service';
import { CommonModule, DatePipe, NgClass } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Class that deals with entry-details operations

@Component({
  selector: 'app-entry-details',
  imports: [NavComponent, FooterComponent, NgClass, DatePipe, CommonModule],
  templateUrl: './entry-details.html',
  styleUrl: './entry-details.css'
})
export class EntryDetailsComponent {

  verification: HistoryModel = {
    verificationId: 0,
    status: '',
    verdictType: '',
    reasoning: '',
    confidenceScore: 0,
    evidences: [],
    date: '',
    claim: '',
    submittedAt: '',
    completedAt: '',
    message: ''
  };

  isLoading = true;

  verificationId!: number;
  private routeSub: Subscription = new Subscription();

  constructor(
    private historyService: HistoryService,
    private tokenStorageService: TokenStorageService,
    private route: ActivatedRoute
  ) {}


  //Takes id in the address bar and returns verification associated with that verificationId
  ngOnInit(): void {

    this.routeSub = this.route.params.subscribe(params => {
      this.verificationId = params['id'];
    })

    this.historyService.getVerification(this.verificationId).subscribe({
      next: (data: HistoryModel) => {
        this.verification.status = data.status ?? 'N/A';
        this.verification.claim = data.claim ?? 'N/A';
        this.verification.verdictType = data.verdictType ?? 'N/A';
        this.verification.reasoning = data.reasoning ?? 'N/A';
        this.verification.confidenceScore = data.confidenceScore ?? 0;
        this.verification.evidences = data.evidences ?? null;
        this.verification.submittedAt = data.submittedAt ?? 'N/A';
        this.verification.completedAt = data.completedAt ?? 'N/A';
        this.verification.message = data.message ?? 'N/A';
        this.isLoading = false;
      },
      error: (err) => {
        console.log(err);
        this.isLoading = false;
      }
    });
  }
}
