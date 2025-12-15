import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../cart.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container" style="padding: 20px;">
      <h2>Shopping Cart</h2>
      <div *ngIf="items.length === 0">Your cart is empty.</div>
      
      <div *ngFor="let item of items" class="cart-item">
        <span>{{item.name}} (x{{item.quantity}}) - \${{item.price * item.quantity}}</span>
      </div>
      
      <div *ngIf="items.length > 0" class="total">
        <h3>Total: \${{total}}</h3>
        <button (click)="checkout()" class="checkout-btn">Checkout</button>
      </div>
    </div>
  `,
  styles: [`
    .cart-item { padding: 10px; border-bottom: 1px solid #ddd; display: flex; justify-content: space-between; }
    .total { margin-top: 20px; font-weight: bold; text-align: right; }
    .checkout-btn { padding: 10px 20px; background: #48bb78; color: white; border: none; border-radius: 5px; font-size: 1.2rem; cursor: pointer; }
    .checkout-btn:hover { background: #38a169; }
  `]
})
export class Cart implements OnInit {
  items: any[] = [];
  total = 0;

  constructor(private cartService: CartService, private http: HttpClient) { }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.items = this.cartService.getItems();
    this.total = this.cartService.getTotal();
  }

  checkout() {
    const userStr = localStorage.getItem('user');
    if (!userStr) {
      alert('Please login to checkout');
      return;
    }
    const user = JSON.parse(userStr);

    // Construct OrderRequestDTO
    // userId, items: [{productId, quantity, price}], totalAmount
    const orderRequest = {
      userId: user.id || 1, // Fallback to 1 if user object incomplete (demo)
      items: this.items.map(i => ({ productId: i.id, quantity: i.quantity, price: i.price })),
      totalAmount: this.total
    };

    this.http.post('http://localhost:8080/order/create', orderRequest).subscribe({
      next: (res) => {
        console.log(res);
        alert('Order Placed Successfully! SAGA sequence initiated.');
        this.cartService.clearCart();
        this.refresh();
      },
      error: (err) => {
        console.error(err);
        alert('Order Failed');
      }
    });
  }
}
