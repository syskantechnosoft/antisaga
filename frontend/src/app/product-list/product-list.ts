import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container" style="padding: 20px;">
      <div class="search-bar">
        <input type="text" [(ngModel)]="searchTerm" placeholder="Search products..." (keyup.enter)="search()" class="search-input">
        <select [(ngModel)]="category" (change)="search()" class="filter-select">
            <option value="">All Categories</option>
            <option value="Electronics">Electronics</option>
            <option value="Fashion">Fashion</option>
        </select>
        <button (click)="search()">Search</button>
      </div>
      
      <div class="product-grid">
        <div *ngFor="let p of products" class="product-card">
          <img [src]="p.imageUrl" alt="Product Image" style="width:100%; height:150px; object-fit:cover;">
          <h3>{{p.name}}</h3>
          <p>{{p.description}}</p>
          <p class="price">\${{p.price}}</p>
          <button (click)="addToCart(p)">Add to Cart</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 20px; margin-top: 20px; }
    .product-card { padding: 15px; background: white; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); transition: transform 0.3s; }
    .product-card:hover { transform: translateY(-5px); box-shadow: 0 5px 15px rgba(0,0,0,0.2); }
    .search-bar { display: flex; gap: 10px; margin-bottom: 20px; }
    .search-input { flex: 1; padding: 10px; border-radius: 5px; border: 1px solid #ccc; }
    button { padding: 10px 20px; background: #667eea; color: white; border: none; border-radius: 5px; cursor: pointer; }
    button:hover { background: #5a6fd1; }
  `]
})
export class ProductList implements OnInit {
  products: any[] = [];
  searchTerm = '';
  category = '';

  constructor(private http: HttpClient, private cartService: CartService) { }

  ngOnInit() {
    this.search();
  }

  search() {
    let url = 'http://localhost:8080/products';
    if (this.searchTerm) url += `?search=${this.searchTerm}`;
    else if (this.category) url += `?category=${this.category}`;

    this.http.get(url).subscribe((res: any) => this.products = res);
  }

  addToCart(product: any) {
    this.cartService.addToCart(product);
    alert('Added to Cart!');
  }
}
