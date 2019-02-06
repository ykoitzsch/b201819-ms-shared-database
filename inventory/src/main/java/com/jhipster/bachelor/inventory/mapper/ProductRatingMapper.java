package com.jhipster.bachelor.inventory.mapper;

import java.util.List;

import com.jhipster.bachelor.inventory.domain.Product;
import com.jhipster.bachelor.inventory.domain.Rating;
import com.jhipster.bachelor.inventory.domain.dto.ProductDto;

public class ProductRatingMapper {

  public static ProductDto mapToProductDto(Product product, List<Rating> productRatings) {
    ProductDto dto = new ProductDto();
    dto.setProductId(product.getId());
    dto.setDescription(product.getDescription());
    dto.setImage(product.getImage());
    dto.setName(product.getName());
    dto.setPrice(product.getPrice());
    dto.setProductCategory(product.getProductCategory());
    dto.setRatings(productRatings);

    return dto;
  }
}
