import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Navbar } from './navbar/navbar';
import { Toast } from './toast/toast';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, Navbar, Toast],
  template: `
    <app-navbar></app-navbar>
    <app-toast></app-toast>
    <div class="container">
      <router-outlet></router-outlet>
    </div>
  `
})
export class App {
  title = 'frontend';
}
