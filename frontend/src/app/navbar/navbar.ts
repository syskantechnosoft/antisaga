import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <nav class="navbar">
      <div class="logo">AntiSaga Shop</div>
      <div class="links">
        <a routerLink="/products">Products</a>
        <a routerLink="/cart">Cart</a>
        <span *ngIf="user">{{user.username}}</span>
        <a *ngIf="!user" routerLink="/login">Login</a>
        <a *ngIf="!user" routerLink="/register">Register</a>
        <a *ngIf="user" (click)="logout()" style="cursor: pointer">Logout</a>
      </div>
    </nav>
  `,
  styles: [`
    .navbar { background: #333; color: white; padding: 1rem; display: flex; justify-content: space-between; align-items: center; }
    .links a { color: white; text-decoration: none; margin-left: 1rem; }
    .links a:hover { text-decoration: underline; }
    .logo { font-weight: bold; font-size: 1.2rem; }
  `]
})
export class Navbar {
  user: any;

  constructor(private router: Router) {
    this.checkUser();
    // primitive state check
    window.addEventListener('storage', () => this.checkUser());
  }

  checkUser() {
    const u = localStorage.getItem('user');
    this.user = u ? JSON.parse(u) : null;
  }

  logout() {
    localStorage.removeItem('user');
    this.user = null;
    this.router.navigate(['/login']);
  }
}
