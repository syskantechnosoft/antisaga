import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class CartService {
    items: any[] = [];

    addToCart(product: any) {
        const existing = this.items.find(i => i.id === product.id);
        if (existing) {
            existing.quantity++;
        } else {
            this.items.push({ ...product, quantity: 1 });
        }
    }

    getItems() {
        return this.items;
    }

    clearCart() {
        this.items = [];
    }

    getTotal() {
        return this.items.reduce((acc, item) => acc + (item.price * item.quantity), 0);
    }
}
