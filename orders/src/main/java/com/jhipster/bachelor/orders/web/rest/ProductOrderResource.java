package com.jhipster.bachelor.orders.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.bachelor.orders.domain.Product;
import com.jhipster.bachelor.orders.domain.ProductOrder;
import com.jhipster.bachelor.orders.domain.dto.ProductOrderDto;
import com.jhipster.bachelor.orders.repository.ProductRepository;
import com.jhipster.bachelor.orders.service.ProductOrderService;
import com.jhipster.bachelor.orders.web.rest.errors.BadRequestAlertException;
import com.jhipster.bachelor.orders.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing ProductOrder.
 */
@RestController
@RequestMapping("/api")
public class ProductOrderResource {

  private final Logger log = LoggerFactory.getLogger(ProductOrderResource.class);

  private static final String ENTITY_NAME = "ordersProductOrder";

  private ProductOrderService productOrderService;

  private ProductRepository productRepository;

  public ProductOrderResource(ProductOrderService productOrderService, ProductRepository productRepository) {
    this.productOrderService = productOrderService;
    this.productRepository = productRepository;
  }

  /**
   * POST /product-orders : Create a new productOrder.
   *
   * @param productOrder the productOrder to create
   * @return the ResponseEntity with status 201 (Created) and with body the new productOrder, or with status 400 (Bad
   *         Request) if the productOrder has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/product-orders")
  @Timed
  public ResponseEntity<ProductOrder> createProductOrder(@RequestBody ProductOrder productOrder) throws URISyntaxException {
    log.debug("REST request to save ProductOrder : {}", productOrder);
    if (productOrder.getId() != null) {
      throw new BadRequestAlertException("A new productOrder cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ProductOrder result = productOrderService.save(productOrder);
    return ResponseEntity
      .created(new URI("/api/product-orders/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * PUT /product-orders : Updates an existing productOrder.
   *
   * @param productOrder the productOrder to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated productOrder, or with status 400 (Bad
   *         Request) if the productOrder is not valid, or with status 500 (Internal Server Error) if the productOrder
   *         couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/product-orders")
  @Timed
  public ResponseEntity<ProductOrder> updateProductOrder(@RequestBody ProductOrder productOrder) throws URISyntaxException {
    log.debug("REST request to update ProductOrder : {}", productOrder);
    if (productOrder.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ProductOrder result = productOrderService.save(productOrder);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productOrder.getId().toString())).body(result);
  }

  /**
   * GET /product-orders : get all the productOrders.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of productOrders in body
   */
  @GetMapping("/product-orders")
  @Timed
  public List<ProductOrder> getAllProductOrders() {
    log.debug("REST request to get all ProductOrders");
    return productOrderService.findAll();
  }

  @GetMapping("/filtered-product-orders")
  @Timed
  @Transactional
  public List<ProductOrderDto> getProductOrdersWithBasketId(@RequestParam(value = "customerId", required = true) String customerId) {

    List<ProductOrder> orders = productOrderService
      .findAll()
      .stream()
      .filter(r -> r.getCustomerId().equals(Long.valueOf(customerId)))
      .collect(Collectors.toList());

    List<ProductOrderDto> dtoList = new ArrayList<>();
    for (ProductOrder p : orders) {
      ProductOrderDto dto = new ProductOrderDto();
      dto.setAmount(p.getAmount());
      dto.setBasket_id(Long.valueOf(customerId));
      dto.setCustomerId(Long.valueOf(customerId));

      Product product = productRepository.getOne(p.getProductId());
      dto.setDescription(product.getDescription());
      dto.setId(p.getId());
      dto.setCategory(product.getProductCategory().getName());
      dto.setImage(product.getImage());
      dto.setName(product.getName());
      dto.setPrice(product.getPrice());
      dtoList.add(dto);
    }

    return dtoList;
  }

  /**
   * GET /product-orders/:id : get the "id" productOrder.
   *
   * @param id the id of the productOrder to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the productOrder, or with status 404 (Not Found)
   */
  @GetMapping("/product-orders/{id}")
  @Timed
  public ResponseEntity<ProductOrder> getProductOrder(@PathVariable Long id) {
    log.debug("REST request to get ProductOrder : {}", id);
    Optional<ProductOrder> productOrder = productOrderService.findOne(id);
    return ResponseUtil.wrapOrNotFound(productOrder);
  }

  /**
   * DELETE /product-orders/:id : delete the "id" productOrder.
   *
   * @param id the id of the productOrder to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/product-orders/{id}")
  @Timed
  public ResponseEntity<Void> deleteProductOrder(@PathVariable Long id) {
    log.debug("REST request to delete ProductOrder : {}", id);
    productOrderService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
