package com.dto.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
public class Bid {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long itemId;
  private Long bidderId;
  private BigDecimal amount;
  private LocalDateTime createdAt;

  public Bid() {

  }

  public Bid(Long id, Long itemId, Long bidderId, BigDecimal amount, LocalDateTime createdAt) {
    this.id = id;
    this.itemId = itemId;
    this.bidderId = bidderId;
    this.amount = amount;
    this.createdAt = createdAt;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Long getBidderId() {
    return bidderId;
  }

  public Long getId() {
    return id;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setBidderId(Long bidderId) {
    this.bidderId = bidderId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
