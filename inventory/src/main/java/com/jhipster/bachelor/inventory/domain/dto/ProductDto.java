package com.jhipster.bachelor.inventory.domain.dto;

import java.util.List;

import com.jhipster.bachelor.inventory.domain.ProductCategory;
import com.jhipster.bachelor.inventory.domain.Rating;

public class ProductDto {

  private Long productId;

  private String name;

  private String description;

  private Double price;

  private String image;

  private ProductCategory productCategory;

  private List<Rating> ratings;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public ProductCategory getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(ProductCategory productCategory) {
    this.productCategory = productCategory;
  }

  public List<Rating> getRatings() {
    return ratings;
  }

  public void setRatings(List<Rating> ratings) {
    this.ratings = ratings;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

}
