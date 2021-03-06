package com.jhipster.bachelor.customers.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.customers.domain.Basket;
import com.jhipster.bachelor.customers.domain.Customer;
import com.jhipster.bachelor.customers.repository.BasketRepository;
import com.jhipster.bachelor.customers.repository.CustomerRepository;

/**
 * Service Implementation for managing Customer.
 */
@Service
@Transactional
public class CustomerService {

  private final Logger log = LoggerFactory.getLogger(CustomerService.class);

  private CustomerRepository customerRepository;

  private BasketRepository basketRepository;

  public CustomerService(CustomerRepository customerRepository, BasketRepository basketRepository) {
    this.customerRepository = customerRepository;
    this.basketRepository = basketRepository;

  }

  /**
   * Save a customer.
   *
   * @param customer the entity to save
   * @return the persisted entity
   */
  public Customer save(Customer customer) {
    log.debug("Request to save Customer : {}", customer);
    Customer c = customerRepository.save(customer);
    Basket basket = new Basket();
    basket.setId(c.getId());
    basket.setCustomerId(c.getId());
    basketRepository.save(basket);
    return c;
  }

  /**
   * Get all the customers.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<Customer> findAll() {
    log.debug("Request to get all Customers");
    return customerRepository.findAll();
  }

  /**
   * Get one customer by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<Customer> findOne(Long id) {
    log.debug("Request to get Customer : {}", id);
    return customerRepository.findById(id);
  }

  /**
   * Delete the customer by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Customer : {}", id);
    customerRepository.deleteById(id);
  }
}
