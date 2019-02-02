import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IBasket } from 'app/shared/model/orders/basket.model';
import { BasketService } from './basket.service';

@Component({
    selector: 'jhi-basket-update',
    templateUrl: './basket-update.component.html'
})
export class BasketUpdateComponent implements OnInit {
    basket: IBasket;
    isSaving: boolean;

    constructor(private basketService: BasketService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ basket }) => {
            this.basket = basket;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.basket.id !== undefined) {
            this.subscribeToSaveResponse(this.basketService.update(this.basket));
        } else {
            this.subscribeToSaveResponse(this.basketService.create(this.basket));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBasket>>) {
        result.subscribe((res: HttpResponse<IBasket>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
