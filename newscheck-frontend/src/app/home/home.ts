import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FooterComponent } from "../components/footer/footer";
import { NavComponent } from "../components/nav/nav";
import { TokenStorageService } from '../services/token-storage-service';
import { HistoryService } from '../services/history-service';
import { HistoryModel } from '../models/history-model';
import { ContentType } from '../models/content-model';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'app-home',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule, FooterComponent, NavComponent, RouterLink],
    templateUrl: './home.html',
})
export class HomeComponent implements OnInit{
    inputText = '';
    inputLink = '';
    selectedFile: File | null = null;
    result: any;
    noResult = '';
    isLoading = false;
    history: any;
    base64String = '';

    constructor(
        private tokenStorageService: TokenStorageService,
        private historyService: HistoryService
    ) {}

    ngOnInit(): void {
    this.historyService.getHistory(this.tokenStorageService.getUsrId()).subscribe({
      next: (data) => {
      this.history = data.map((item: HistoryModel) => ({
        verificationId: item.verificationId,
        status: item.status ?? 'N/A',
        verdictType: item.verdictType ?? '—',
        reasoning: item.reasoning ?? 'N/A',
        confidenceScore: item.confidenceScore ?? 0,
        evidences: item.evidences ?? null,
        date: item.submittedAt ?? 'N/A',
        claim: item.claim ?? 'N/A'
      }));
      this.history = this.history.slice(0,3);
    },
      error: (err) => {
        console.log(err);
      }
    })
  }


    onFileSelected(event: Event): void {
        const input = event.target as HTMLInputElement;
        if (input.files && input.files.length > 0) {
                this.selectedFile = input.files[0];

                const reader = new FileReader();

                reader.onload = () => {
                    this.base64String = reader.result as string; 
                };

                reader.readAsDataURL(this.selectedFile);
                
            }
        }


    async analyzeInput(): Promise<void> {
        if (!this.inputText && !this.inputLink && !this.selectedFile) {
            this.result = '';
            this.noResult = 'Please provide text, a link, or an uploaded file to analyze.';
        }
        else {

            this.isLoading = true;     
            this.result = '';        
            this.noResult = '';        


           if (this.inputText) {
             const id = this.tokenStorageService.getUsrId()

             this.historyService.submitText(id, ContentType.TEXT, this.inputText).subscribe({
                next: (data: HistoryModel) => {
                    this.result = {               
                        status: data.status,
                        claim: data.claim,
                        verdict: data.verdictType,
                        reasoning: data.reasoning,
                        submittedAt: data.submittedAt
                    };

                     const newEntry = {
                        verificationId: data.verificationId,
                        date: data.submittedAt,
                        claim: data.claim,
                        status: data.status,
                        verdictType: data.verdictType
                    };

                    this.history.unshift(newEntry);
                    this.history = this.history.slice(0,3);
                    this.isLoading = false;
                },
                error: (err) => {
                    this.noResult = "The system cannot accommodate requests at this time. Please try again later.";
                    this.isLoading = false;
                }
            })
           }

           if (this.inputLink) {
            const id = this.tokenStorageService.getUsrId()

             this.historyService.submitLink(id, ContentType.URL, this.inputLink).subscribe({
                next: (data: HistoryModel) => {
                    this.result = {               
                        status: data.status,
                        claim: data.claim,
                        verdict: data.verdictType,
                        reasoning: data.reasoning,
                        submittedAt: data.submittedAt
                    };

                    const newEntry = {
                        verificationId: data.verificationId,
                        date: data.submittedAt,
                        claim: data.claim,
                        status: data.status,
                        verdictType: data.verdictType
                    };

                    this.history.unshift(newEntry);
                    this.history = this.history.slice(0,3);
                    this.isLoading = false;
                },
                error: (err) => {
                    this.noResult = "The system cannot accommodate requests at this time. Please try again later.";
                    this.isLoading = false;
                }
            })
           }
 
           if (this.selectedFile) {

            const id = this.tokenStorageService.getUsrId()

            console.log(this.base64String);

            this.historyService.submitImage(id, ContentType.IMAGE, this.base64String).subscribe({
                next: (data: HistoryModel) => {
                    this.result = {               
                        status: data.status,
                        claim: data.claim,
                        verdict: data.verdictType,
                        reasoning: data.reasoning,
                        submittedAt: data.submittedAt
                    };
                    
                    const newEntry = {
                        verificationId: data.verificationId,
                        date: data.submittedAt,
                        claim: data.claim,
                        status: data.status,
                        verdictType: data.verdictType
                    };

                    this.history.unshift(newEntry);
                    this.history = this.history.slice(0,3);
                    this.isLoading = false;
                },
                error: (err) => {
                    this.noResult = "The system cannot accommodate requests at this time. Please try again later.";
                    this.isLoading = false;
                }
            })

           }

            this.inputText = '';
        }

    }

    scrollToTop() {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}
