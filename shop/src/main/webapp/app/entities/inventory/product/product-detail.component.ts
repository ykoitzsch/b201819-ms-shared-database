import { ProductOrderService } from '../../orders/product-order/product-order.service';
import { RatingService } from '../../ratings/rating/rating.service';
import { Rating } from '../../../shared/model/ratings/rating.model';
import { JhiAlertService } from 'ng-jhipster';
import { Basket } from 'app/shared/model/orders/basket.model';
import { AccountService } from '../../../core/auth/account.service';
import { ProductOrder } from '../../../shared/model/orders/product-order.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { IProduct } from 'app/shared/model/inventory/product.model';
import { BasketService } from '../../orders/basket';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-product-detail',
    templateUrl: './product-detail.component.html'
})
export class ProductDetailComponent implements OnInit {
    product: IProduct;
    amount: number;
    account: Account;
    basket: Basket;
    ratings: Rating[];
    currentRating: number;
    customerDesc: String;
    customerPoints: number;
    customerRatedAlready: boolean;
    ratingSubmitButtonActive: boolean;

    constructor(
        private activatedRoute: ActivatedRoute,
        private accountService: AccountService,
        private basketService: BasketService,
        private jhiAlertService: JhiAlertService,
        private ratingService: RatingService,
        private productOrderService: ProductOrderService,
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ product }) => {
            this.product = product;
            this.amount = 1;
            this.customerDesc = '';
            this.customerPoints = 1;
            this.currentRating = 0;
            this.customerRatedAlready = false;
            this.ratingSubmitButtonActive = false;
            this.ratings = product.ratings;
        });

        this.accountService.get().subscribe(response => {
            if (response.status === 200) {
                this.account = response.body;
            }
        });
    }

    previousState() {
        window.history.back();
    }

    intoBasket(amount, product) {
        console.log(product);
        this.productOrderService.create(new ProductOrder(null, amount, +this.account.id, product.productId, null, null)).subscribe(
            (r: HttpResponse<Basket>) => {
                this.jhiAlertService.success(amount + 'x ' + product.name + ' has successfully been added to your basket!');
            });
    }

    textAreaEmpty() {
        if (this.customerDesc.trim() !== '') {
            this.ratingSubmitButtonActive = true;
        } else {
            this.ratingSubmitButtonActive = false;
        }
    }

    calculateRating() {
        this.currentRating = 0;
        for (const r of this.product.ratings) {
            this.currentRating = this.currentRating + r.points;
        }
        this.currentRating = +Number(this.currentRating / this.ratings.length).toFixed(2);
    }

    rate(points, desc) {
        if (!this.ratingExists()) {
            this.ratingService.create(new Rating(null, points, this.product.productId, +this.account.id, desc)).subscribe(
                (res: HttpResponse<Rating>) => {
                    this.product.ratings.push(res.body);
                    this.calculateRating();
                    this.customerRatedAlready = true;
                },
                (res: HttpErrorResponse) => {
                    this.jhiAlertService.error(res.message + ' Rating could not been saved');
                }
            );
        } else {
            this.jhiAlertService.error('You have already rated this product');
        }
    }
    ratingExists() {
        for (const r of this.product.ratings) {
            if (r.customerId === +this.account.id) {
                this.customerRatedAlready = true;
                return true;
            }
        }
        return false;
    }
}
