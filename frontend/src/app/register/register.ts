import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="container" style="max-width: 400px; margin-top: 50px;">
      <h2>Register</h2>
      <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
        
        <div class="form-group">
          <label>Username</label>
          <input type="text" formControlName="username" class="form-control" 
                 [class.is-invalid]="f['username'].invalid && f['username'].touched">
          <div *ngIf="f['username'].invalid && f['username'].touched" class="error-msg">
            Username is required.
          </div>
        </div>

        <div class="form-group">
          <label>Email</label>
          <input type="email" formControlName="email" class="form-control"
                 [class.is-invalid]="f['email'].invalid && f['email'].touched">
          <div *ngIf="f['email'].invalid && f['email'].touched" class="error-msg">
            <div *ngIf="f['email'].errors?.['required']">Email is required.</div>
            <div *ngIf="f['email'].errors?.['email']">Invalid email format.</div>
          </div>
        </div>

        <div class="form-group">
          <label>Mobile</label>
          <input type="text" formControlName="mobile" class="form-control"
                 [class.is-invalid]="f['mobile'].invalid && f['mobile'].touched" maxlength="10">
          <div *ngIf="f['mobile'].invalid && f['mobile'].touched" class="error-msg">
            <div *ngIf="f['mobile'].errors?.['required']">Mobile is required.</div>
            <div *ngIf="f['mobile'].errors?.['pattern']">Must be 10 digits starting with 6-9.</div>
          </div>
        </div>

        <div class="form-group">
          <label>Password</label>
          <input type="password" formControlName="password" class="form-control"
                 [class.is-invalid]="f['password'].invalid && f['password'].touched">
          <div *ngIf="f['password'].invalid && f['password'].touched" class="error-msg">
            Password is required.
          </div>
        </div>

        <button type="submit" class="btn-submit" [disabled]="registerForm.invalid">Register</button>
      </form>
    </div>
  `,
  styles: [`
    .form-control { width: 100%; padding: 10px; margin-bottom: 5px; border-radius: 5px; border: 1px solid #ddd; }
    .is-invalid { border-color: #dc3545; }
    .error-msg { color: #dc3545; font-size: 0.875em; margin-bottom: 15px; }
    .btn-submit { width: 100%; padding: 10px; background: #667eea; color: white; border: none; border-radius: 5px; cursor: pointer; }
    .btn-submit:disabled { background: #ccc; cursor: not-allowed; }
  `]
})
export class Register {
  registerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private toast: ToastService
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobile: ['', [Validators.required, Validators.pattern('^[6-9][0-9]{9}$')]]
    });
  }

  get f() { return this.registerForm.controls; }

  onSubmit() {
    if (this.registerForm.invalid) return;

    this.http.post('http://localhost:8080/auth/register', this.registerForm.value)
      .subscribe({
        next: () => {
          this.toast.show('Registration Successful', 'success');
          this.router.navigate(['/login']);
        },
        error: (err) => {
          // handled by interceptor but we can add specific handling if needed
        }
      });
  }
}
