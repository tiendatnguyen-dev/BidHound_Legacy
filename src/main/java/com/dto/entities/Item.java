package com.dto.entities;

import com.dto.util.AuctionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@Entity
@Table(name = "items",
       indexes = {
        @Index(name = "endtime_status", columnList = "auction_status, end_time"),
        @Index(name = "seller_id", columnList = "seller_id")
       })
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private BigDecimal currentPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", nullable = false)
  private User seller;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "winner_id", nullable = true)
  private User winner;
  private LocalDateTime endTime;

  @Enumerated(EnumType.STRING)
  private AuctionStatus auctionStatus;

  @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
  private final List<Bid> bidHistory = new ArrayList<>();

  public Item() {
  }

  public Item(Long id, String title, BigDecimal currentPrice, User winner, User seller) {
    this.id = id;
    this.title = title;
    this.currentPrice = currentPrice;
    this.winner = winner;
    this.seller = seller;
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

  public void addBid(Bid bid) {
    bidHistory.add(bid);
  }

  public List<Bid> getBidHistory() {
    return bidHistory;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public User getSeller() {
    return seller;
  }

  public User getWinner() {
    return winner;
  }

  public void setSeller(User seller) {
    this.seller = seller;
  }

  public void setWinner(User winner) {
    this.winner = winner;
  }

  public AuctionStatus getAuctionStatus() {
    return auctionStatus;
  }

  public void setAuctionStatus(AuctionStatus auctionStatus) {
    this.auctionStatus = auctionStatus;
  }
}


