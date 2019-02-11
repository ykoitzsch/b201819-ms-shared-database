import { ProductOrderDto } from './../../../shared/model/orders/productOrderDto.model';
import { ProductOrderService } from '../product-order/product-order.service';
import { User } from '../../../core/user/user.model';
import { Subscription } from 'rxjs';
import { AccountService, Principal } from 'app/core';
import { CompleteOrderService } from '../complete-order/complete-order.service';
import { CompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { BasketService } from 'app/entities/orders/basket';
import { ProductOrder } from '../../../shared/model/orders/product-order.model';
import { JhiAlertService } from 'ng-jhipster';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBasket } from 'app/shared/model/orders/basket.model';
import { ProductService, ProductDeletePopupComponent } from '../../inventory/product';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Product } from '../../../shared/model/inventory/product.model';
import { OrderStatus } from '../../../shared/model/orders/complete-order.model';

class ProductDto {
    constructor(
        private amount?: number,
        private name?: String,
        private price?: number,
        private image?: String,
        private description?: String,
        private category?: String,
        private productOrder?: ProductOrder
    ) {}
}

@Component({
    selector: 'jhi-basket-detail',
    templateUrl: './my-basket.component.html'
})
export class MyBasketComponent implements OnInit {
    basket: IBasket;
    productOrders: ProductOrderDto[] = [];
    totalProducts: number;
    totalPrice: number;
    currentUser: User;

    constructor(
        private productOrderService: ProductOrderService,
        private jhiAlertService: JhiAlertService,
        private completeOrderService: CompleteOrderService,
        private accountService: AccountService
    ) {}

    ngOnInit() {
        this.accountService.get().subscribe(
            (res: HttpResponse<User>) => {
                this.currentUser = res.body;
                this.loadAllProducts(res.body.id);
            });
    }

    previousState() {
        window.history.back();
    }

    orderNow() {
        console.log("sup");
        if (this.productOrders.length > 0) {
            this.completeOrderService
                .create(
                    new CompleteOrder(
                        null,
                        null,
                        OrderStatus.PENDING,
                        this.currentUser.id,
                        this.totalPrice,
                        new Date(),
                        null
                    )
                )
                .subscribe(
                    (res: HttpResponse<CompleteOrder>) => {
                        this.productOrders = [];
                        this.totalProducts = 0;
                        this.totalPrice = 0.0;
                    },
                    (res: HttpErrorResponse) => {
                        console.log(res);
                    }
                );
        }
    }


    loadAllProducts(id) {
        this.totalProducts = 0;
        this.totalPrice = 0.0;
        this.productOrderService.findByCustomerId({ customerId: id }).subscribe(
            (res: HttpResponse<ProductOrderDto[]>) => {
                this.productOrders = res.body;
                this.totalProducts = this.productOrders.length;
                for(let order of this.productOrders) {
                    this.totalPrice += order.price;
                }
            });
    }

    remove(productOrder) {
        this.productOrderService.delete(productOrder.id).subscribe(
            (res: HttpResponse<ProductOrder>) => {
                this.jhiAlertService.success(productOrder.name + ' has been removed from the basket');
                this.productOrders.splice(this.productOrders.indexOf(productOrder), 1);
                this.totalProducts = this.totalProducts - productOrder.amount;
                this.totalPrice = this.totalPrice - productOrder.amount * productOrder.price;
            },
            (res: HttpErrorResponse) => {
                this.jhiAlertService.error(res.status + ': Could not delete productOrder with id ' + productOrder.id);
            }
        );
    }

}
