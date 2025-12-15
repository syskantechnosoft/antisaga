import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container" style="max-width: 400px; margin-top: 50px;">
      <h2>Login</h2>
      <div class="form-group">
        <label>Username</label>
        <input type="text" [(ngModel)]="username" name="username" class="form-control" required>
      </div>
      <div class="form-group">
        <label>Password</label>
        <input type="password" [(ngModel)]="password" name="password" class="form-control" required>
      </div>
      <button (click)="login()" class="btn-submit">Login</button>
      <p *ngIf="error" style="color: red">{{error}}</p>
    </div>
  `,
  styles: [`
    .form-control { width: 100%; padding: 10px; margin-bottom: 20px; border-radius: 5px; border: 1px solid #ddd; }
    .btn-submit { width: 100%; padding: 10px; background: #667eea; color: white; border: none; border-radius: 5px; cursor: pointer; }
  `]
})
export class Login {
  username = '';
  password = '';
  error = '';

  constructor(private http: HttpClient, private router: Router) { }

  login() {
    this.http.post('http://localhost:8080/auth/login', { username: this.username, password: this.password })
      .subscribe({
        next: (res: any) => {
          localStorage.setItem('user', JSON.stringify(res));
          // Emit event to update navbar if using window listener (hacky but works for demo)
          window.dispatchEvent(new Event('storage'));
          this.router.navigate(['/products']);
        },
        error: () => this.error = 'Invalid Credentials'
      });
  }
}
