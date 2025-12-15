import { Injectable, signal } from '@angular/core';

export interface ToastMessage {
    text: string;
    type: 'success' | 'error';
}

@Injectable({
    providedIn: 'root'
})
export class ToastService {
    toasts = signal<ToastMessage[]>([]);

    show(text: string, type: 'success' | 'error' = 'success') {
        this.toasts.update(current => [...current, { text, type }]);
        setTimeout(() => this.remove(0), 3000); // Auto remove after 3s
    }

    remove(index: number) {
        this.toasts.update(current => current.filter((_, i) => i !== index));
    }
}
