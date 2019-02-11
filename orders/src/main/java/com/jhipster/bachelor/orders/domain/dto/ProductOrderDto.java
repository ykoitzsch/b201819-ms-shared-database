package com.jhipster.bachelor.orders.domain.dto;

public class ProductOrderDto {

  private Long id;

  private Integer amount;

  private Long customerId;

  private long basket_id;

  private String name;

  private String description;

  private Double price;

  private String image;

  private String category;

  public Long getId() {
    return id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public long getBasket_id() {
    return basket_id;
  }

  public void setBasket_id(long basket_id) {
    this.basket_id = basket_id;
  }

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

}
