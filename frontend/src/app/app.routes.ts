import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { ProductList } from './product-list/product-list';
import { Cart } from './cart/cart';
import { Order } from './order/order';

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    { path: 'products', component: ProductList },
    { path: 'cart', component: Cart },
    { path: 'order', component: Order },
    { path: '', redirectTo: 'products', pathMatch: 'full' }
];
