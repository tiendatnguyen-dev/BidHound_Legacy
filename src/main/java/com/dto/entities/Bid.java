package com.dto.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids",
        indexes = {
        @Index(name = "item_bidder_id", columnList = "item_id, bidder_id"),
        @Index(name = "created_at", columnList = "created_at")
        })
public class Bid {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bidder_id", nullable = false)
  private User user;

  private BigDecimal amount;
  private LocalDateTime createdAt;

  public Bid() {

  }

  public Bid(Long id, Item item, User user, BigDecimal amount, LocalDateTime createdAt) {
    this.id = id;
    this.item = item;
    this.user = user;
    this.amount = amount;
    this.createdAt = createdAt;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Long getId() {
    return id;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Item getItem() {
    return item;
  }

  public User getUser() {
    return user;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
