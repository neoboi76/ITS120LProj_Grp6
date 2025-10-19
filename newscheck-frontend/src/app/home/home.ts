import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FooterComponent } from "../components/footer/footer";
import { NavComponent } from "../components/nav/nav";

@Component({
    selector: 'app-home',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule, FooterComponent, NavComponent],
    templateUrl: './home.html',
})
export class HomeComponent {
    inputText = '';
    inputLink = '';
    selectedFile: File | null = null;
    result = '';
    noResult = '';
    isLoading = false;

    history = [
        { date: '2025-10-05', news: 'Did Cardinal Luis Tagle speak about corruption?', status: 'Verified', verdict: 'True' },
        { date: '2025-05-10', news: 'Scientists announced immortality formula', status: 'Verified', verdict: 'False' },
        { date: '2025-10-05', news: 'Stock Market Rise caused by AI robots', status: 'Verified', verdict: 'False' },
    ];


    onFileSelected(event: Event): void {
        const input = event.target as HTMLInputElement;
        if (input.files && input.files.length > 0) {
            this.selectedFile = input.files[0];
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

            await new Promise(resolve => setTimeout(resolve, 3000));

            this.result =
                'After analyzing the provided information, the AI system determined that the source appears credible. ' +
                'However, cross-verification with multiple sources is still recommended for accuracy.';

            this.isLoading = false;   

            this.inputText = '';
        }

    }
}
