import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container" style="max-width: 400px; margin-top: 50px;">
      <h2>Register</h2>
      <div class="form-group">
        <label>Username</label>
        <input type="text" [(ngModel)]="user.username" name="username" class="form-control">
      </div>
      <div class="form-group">
        <label>Email</label>
        <input type="email" [(ngModel)]="user.email" name="email" class="form-control">
      </div>
      <div class="form-group">
        <label>Mobile</label>
        <input type="text" [(ngModel)]="user.mobile" name="mobile" class="form-control">
      </div>
      <div class="form-group">
        <label>Password</label>
        <input type="password" [(ngModel)]="user.password" name="password" class="form-control">
      </div>
      <button (click)="register()" class="btn-submit">Register</button>
    </div>
  `,
  styles: [`
    .form-control { width: 100%; padding: 10px; margin-bottom: 20px; border-radius: 5px; border: 1px solid #ddd; }
    .btn-submit { width: 100%; padding: 10px; background: #667eea; color: white; border: none; border-radius: 5px; cursor: pointer; }
  `]
})
export class Register {
  user = { username: '', password: '', email: '', mobile: '' };

  constructor(private http: HttpClient, private router: Router) { }

  register() {
    this.http.post('http://localhost:8080/auth/register', this.user)
      .subscribe(() => {
        alert('Registration Successful');
        this.router.navigate(['/login']);
      });
  }
}
