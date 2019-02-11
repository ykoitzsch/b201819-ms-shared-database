package com.jhipster.bachelor.orders.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.orders.domain.Basket;
import com.jhipster.bachelor.orders.domain.CompleteOrder;
import com.jhipster.bachelor.orders.domain.Invoice;
import com.jhipster.bachelor.orders.domain.ProductOrder;
import com.jhipster.bachelor.orders.domain.enumeration.InvoiceStatus;
import com.jhipster.bachelor.orders.domain.enumeration.OrderStatus;
import com.jhipster.bachelor.orders.repository.BasketRepository;
import com.jhipster.bachelor.orders.repository.CompleteOrderRepository;
import com.jhipster.bachelor.orders.repository.InvoiceRepository;

/**
 * Service Implementation for managing CompleteOrder.
 */
@Service
@Transactional
public class CompleteOrderService {

  private final Logger log = LoggerFactory.getLogger(CompleteOrderService.class);

  private CompleteOrderRepository completeOrderRepository;

  private InvoiceRepository invoiceRepository;

  private BasketRepository basketRepository;

  private ProductOrderService productOrderService;

  public CompleteOrderService(
      CompleteOrderRepository completeOrderRepository,
      BasketRepository basketRepository,
      ProductOrderService productOrderService,
      InvoiceRepository invoiceRepository) {
    this.completeOrderRepository = completeOrderRepository;
    this.basketRepository = basketRepository;
    this.productOrderService = productOrderService;
    this.invoiceRepository = invoiceRepository;
  }

  /**
   * Save a completeOrder.
   *
   * @param completeOrder the entity to save
   * @return the persisted entity
   * @throws Exception
   */
  public CompleteOrder save(CompleteOrder completeOrder) throws Exception {
    if (completeOrder.getStatus().equals(OrderStatus.COMPLETED) && completeOrder.getId() != null) {
      Invoice i = new Invoice();
      i.setAmount(completeOrder.getTotalPrice());
      i.setCode(UUID.randomUUID().toString());
      i.setCustomerId(completeOrder.getCustomerId());
      i.setDueDate(new Date().toString());
      i.setOrderId(completeOrder.getId());
      i.setPaymentDate(new Date().toString());
      i.setStatus(InvoiceStatus.PAID);
      Invoice savedInvoice = invoiceRepository.save(i);
      completeOrder.setInvoiceId(savedInvoice.getId());
      return completeOrderRepository.save(completeOrder);
    } else if (completeOrder.getId() == null) {
      log.debug("Request to save CompleteOrder : {}", completeOrder);
      CompleteOrder result;
      Optional<Basket> optBasket = basketRepository.findById(completeOrder.getCustomerId());
      if (optBasket.isPresent()) {
        Basket basket = optBasket.get();
        basket.getProductOrders().clear();
        this.basketRepository.save(basket); //clear basket
        Set<ProductOrder> orders = this.productOrderService
          .findAll()
          .stream()
          .filter(r -> r.getCustomerId().equals(completeOrder.getCustomerId()))
          .collect(Collectors.toSet());
        completeOrder.setProductOrders(orders);
        result = completeOrderRepository.save(completeOrder);
        orders.forEach(p -> {
          p.setBasket(null);
          p.setCompleteOrder(completeOrder);
          this.productOrderService.save(p);
        });
      } else
        throw new Exception("Basket with ID " + completeOrder.getCustomerId() + " does not exist");

      return result;
    }
    return completeOrderRepository.save(completeOrder);
  }

  /**
   * Get all the completeOrders.
   * 
   * @param customerId
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<CompleteOrder> findAll() {
    log.debug("Request to get all CompleteOrders");
    return completeOrderRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<CompleteOrder> findByCustomerId(String customerId) {
    return completeOrderRepository.findByCustomerId(Long.valueOf(customerId));
  }

  /**
   * Get one completeOrder by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<CompleteOrder> findOne(Long id) {
    log.debug("Request to get CompleteOrder : {}", id);
    return completeOrderRepository.findById(id);
  }

  /**
   * Delete the completeOrder by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete CompleteOrder : {}", id);
    completeOrderRepository.deleteById(id);
  }
}
