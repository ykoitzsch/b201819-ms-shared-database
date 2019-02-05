import { Account } from './../../../core/user/account.model';
import { AccountService } from './../../../core/auth/account.service';
import { InvoiceService } from './../../invoices/invoice/invoice.service';
import { Invoice, InvoiceStatus } from './../../../shared/model/invoices/invoice.model';
import { Product } from 'app/shared/model/inventory/product.model';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription, ObjectUnsubscribedError } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { v4 } from 'uuid';

import { ICompleteOrder } from 'app/shared/model/orders/complete-order.model';
import { Principal, AccountService, User } from 'app/core';
import { CompleteOrderService } from './complete-order.service';
import { ProductService } from '../../inventory/product';
import { OrderStatus } from '../../../shared/model/orders/complete-order.model';

@Component({
    selector: 'jhi-complete-order',
    templateUrl: './complete-order-overview.component.html'
})
export class CompleteOrderOverviewComponent implements OnInit, OnDestroy {
    completeOrders: ICompleteOrder[];
    currentAccount: any;
    eventSubscriber: Subscription;
    products: Product[];
    invoice: Invoice;
    currentUser: User;

    constructor(
        private completeOrderService: CompleteOrderService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private productService: ProductService,
        private accountService: AccountService
    ) {}

    loadAll() {
        this.productService.query().subscribe(res => (this.products = res.body));
        this.accountService.get().subscribe(
            (res: HttpResponse<User>) => {
                this.completeOrderService.findByCustomerId({ customerId: res.body.id, login: res.body.login }).subscribe(
                    (r: HttpResponse<ICompleteOrder[]>) => {
                        this.completeOrders = r.body;
                    },
                    (r: HttpErrorResponse) => this.onError(r.message)
                );
            },
            (res: HttpErrorResponse) => this.jhiAlertService.error(res.message)
        );
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.loadAll();
        });

        this.registerChangeInCompleteOrders();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    getProduct(productId) {
        console.log(productId);
        for (const p of this.products) {
            if (p.id === productId) {
                console.log(p);
                return p;
            }
        }
    }

    payNow(order) {
        order.status = OrderStatus.COMPLETED;
        this.completeOrderService.update(order).subscribe(
            (r: HttpResponse<ICompleteOrder>) => {
                this.jhiAlertService.success('Order has been paid');
            },
            (r: HttpErrorResponse) => {
                this.jhiAlertService.error(r.error, null, null);
                order.status = OrderStatus.PENDING;
            }
        )
    }

    hide(order) {
        this.completeOrders.splice(this.completeOrders.indexOf(order), 1);
    }

    generateOrderNo(timestamp) {
        return timestamp.replace(/[^a-zA-Z0-9]/g, '');
    }
    trackId(index: number, item: ICompleteOrder) {
        return item.id;
    }

    registerChangeInCompleteOrders() {
        this.eventSubscriber = this.eventManager.subscribe('completeOrderListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
