package com.jhipster.bachelor.inventory.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jhipster.bachelor.inventory.domain.Product;
import com.jhipster.bachelor.inventory.domain.Rating;
import com.jhipster.bachelor.inventory.domain.dto.ProductDto;
import com.jhipster.bachelor.inventory.mapper.ProductRatingMapper;
import com.jhipster.bachelor.inventory.repository.ProductRepository;
import com.jhipster.bachelor.inventory.repository.RatingRepository;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductService {

  private final Logger log = LoggerFactory.getLogger(ProductService.class);

  private ProductRepository productRepository;

  private RatingRepository ratingRepository;

  public ProductService(ProductRepository productRepository, RatingRepository ratingRepository) {
    this.productRepository = productRepository;
    this.ratingRepository = ratingRepository;
  }

  /**
   * Save a product.
   *
   * @param product the entity to save
   * @return the persisted entity
   */
  public Product save(Product product) {
    log.debug("Request to save Product : {}", product);
    return productRepository.save(product);
  }

  /**
   * Get all the products.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<Product> findAll() {
    log.debug("Request to get all Products");
    return productRepository.findAll();
  }

  /**
   * Get one product by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public ProductDto findOne(Long id) {
    log.debug("Request to get Product : {}", id);
    Optional<Product> product = productRepository.findById(id);
    if (product.isPresent()) {
      List<Rating> productRatings = ratingRepository.findByProductId(id);
      return ProductRatingMapper.mapToProductDto(product.get(), productRatings);
    }
    return null;
  }

  /**
   * Delete the product by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Product : {}", id);
    productRepository.deleteById(id);
  }
}
