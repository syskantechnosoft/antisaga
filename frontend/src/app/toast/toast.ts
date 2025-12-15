import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container">
      <div *ngFor="let toast of toastService.toasts(); let i = index" 
           class="toast" 
           [ngClass]="toast.type">
        {{ toast.text }}
        <span class="close" (click)="toastService.remove(i)">×</span>
      </div>
    </div>
  `,
  styles: [`
    .toast-container { position: fixed; top: 20px; right: 20px; z-index: 1000; display: flex; flex-direction: column; gap: 10px; }
    .toast { padding: 15px 20px; border-radius: 5px; color: white; min-width: 250px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 4px 6px rgba(0,0,0,0.1); animation: slideIn 0.3s ease-out; }
    .toast.success { background-color: #48bb78; }
    .toast.error { background-color: #f56565; }
    .close { cursor: pointer; margin-left: 10px; font-weight: bold; }
    @keyframes slideIn {
      from { transform: translateX(100%); opacity: 0; }
      to { transform: translateX(0); opacity: 1; }
    }
  `]
})
export class Toast {
  constructor(public toastService: ToastService) { }
}
