import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container" style="max-width: 400px; margin-top: 50px;">
      <h2>Login</h2>
      <div class="form-group">
        <label>Email</label>
        <input type="email" [(ngModel)]="email" name="email" class="form-control" required email>
      </div>
      <div class="form-group">
        <label>Password</label>
        <input type="password" [(ngModel)]="password" name="password" class="form-control" required>
      </div>
      <button (click)="login()" class="btn-submit" [disabled]="!email || !password">Login</button>
    </div>
  `,
  styles: [`
    .form-control { width: 100%; padding: 10px; margin-bottom: 20px; border-radius: 5px; border: 1px solid #ddd; }
    .btn-submit { width: 100%; padding: 10px; background: #667eea; color: white; border: none; border-radius: 5px; cursor: pointer; }
    .btn-submit:disabled { background: #ccc; }
  `]
})
export class Login {
  email = '';
  password = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private toast: ToastService
  ) { }

  login() {
    this.http.post('http://localhost:8080/auth/login', { email: this.email, password: this.password })
      .subscribe({
        next: (res: any) => {
          localStorage.setItem('user', JSON.stringify(res));
          window.dispatchEvent(new Event('storage'));
          this.toast.show('Login Successful!', 'success');
          this.router.navigate(['/products']);
        },
        error: (err) => {
          // Handled by Interceptor usually, but local processing:
        }
      });
  }
}
