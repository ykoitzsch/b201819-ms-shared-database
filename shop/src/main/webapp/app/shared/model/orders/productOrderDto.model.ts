export class ProductOrderDto {
    constructor(
        public id?: number,
        public amount?: number,
        public customerId?: number,
        public productId?: number,
        public basketId?: number,
        public name?: string,
        public description?: string,
        public price?: number,
        public image?: string
    ) {}
}
