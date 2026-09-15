package com.dto.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArraySet;

@Entity
@Table(name = "items")
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private BigDecimal currentPrice;
  private Long sellerId;
  private Long winnerId;
  private LocalDateTime endTime;
  private String status; // ACTIVE - ENDED

  @Transient
  private final CopyOnWriteArraySet<Bid> bidHistory = new CopyOnWriteArraySet<>();

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

  public Long getWinnerId() {
    return winnerId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setWinnerId(Long winnerId) {
    this.winnerId = winnerId;
  }

  public void addBid(Bid bid) {
    bidHistory.add(bid);
  }
  public CopyOnWriteArraySet<Bid> getBidHistory() {
    return bidHistory;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }
}


