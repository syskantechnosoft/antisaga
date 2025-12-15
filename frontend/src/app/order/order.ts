import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../order'; // Importing from ../order.ts

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order.html',
  styleUrl: './order.css',
})
export class Order {
  orderData = {
    userId: 1,
    productId: 1,
    quantity: 1,
    totalAmount: 100
  };
  response: any;
  message: string = '';

  constructor(private orderService: OrderService) { }

  onSubmit() {
    this.orderService.createOrder(this.orderData).subscribe({
      next: (res) => {
        this.response = res;
        this.message = 'Order Created! SAGA triggered.';
      },
      error: (err) => {
        console.error(err);
        this.message = 'Error creating order.';
      }
    });
  }
}
