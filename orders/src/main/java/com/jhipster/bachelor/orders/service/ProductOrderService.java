package com.jhipster.bachelor.orders.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.orders.domain.Basket;
import com.jhipster.bachelor.orders.domain.ProductOrder;
import com.jhipster.bachelor.orders.repository.BasketRepository;
import com.jhipster.bachelor.orders.repository.ProductOrderRepository;

/**
 * Service Implementation for managing ProductOrder.
 */
@Service
@Transactional
public class ProductOrderService {

  private final Logger log = LoggerFactory.getLogger(ProductOrderService.class);

  private ProductOrderRepository productOrderRepository;

  private BasketRepository basketRepository;

  public ProductOrderService(ProductOrderRepository productOrderRepository, BasketRepository basketRepository) {
    this.productOrderRepository = productOrderRepository;
    this.basketRepository = basketRepository;
  }

  /**
   * Save a productOrder.
   *
   * @param productOrder the entity to save
   * @return the persisted entity
   */
  public ProductOrder save(ProductOrder productOrder) {
    log.debug("Request to save ProductOrder : {}", productOrder);
    if (productOrder.getId() == null) {
      Basket b = basketRepository.getOne(productOrder.getCustomerId());
      productOrder.setBasket(b);
    }
    return productOrderRepository.save(productOrder);
  }

  /**
   * Get all the productOrders.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<ProductOrder> findAll() {
    log.debug("Request to get all ProductOrders");
    return productOrderRepository.findAll();
  }

  /**
   * Get one productOrder by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<ProductOrder> findOne(Long id) {
    log.debug("Request to get ProductOrder : {}", id);
    return productOrderRepository.findById(id);
  }

  /**
   * Delete the productOrder by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete ProductOrder : {}", id);
    productOrderRepository.deleteById(id);
  }
}
