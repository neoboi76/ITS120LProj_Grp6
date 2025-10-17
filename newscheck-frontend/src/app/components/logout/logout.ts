import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-logout',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './logout.html',
})
export class LogoutPopupComponent {
    @Output() close = new EventEmitter<boolean>();

    handleResponse(logout: boolean) {
        this.close.emit(logout);
    }
}
