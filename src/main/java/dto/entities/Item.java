package dto.entities;

import java.math.BigDecimal;

public class Item {
  private Long id;
  private String title;
  private BigDecimal currentPrice;
  private Long sellerId;

  public Item() {
  }

  public Item(Long id, String title, BigDecimal currentPrice, Long sellerId) {
    this.id = id;
    this.title = title;
    this.currentPrice = currentPrice;
    this.sellerId = sellerId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public BigDecimal getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(BigDecimal currentPrice) {
    this.currentPrice = currentPrice;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public void setSellerId(Long sellerId) {
    this.sellerId = sellerId;
  }
}
