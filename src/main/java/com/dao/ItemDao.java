package com.dao;

import com.dto.entities.Item;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ItemDao {
  Item findById(Long id);
  void updateCurrentPriceAndWinner(Long itemId, BigDecimal price, Long winnerId);
  void updateStatus(Long itemId, String status);
  void updateEndTime(Long itemId, LocalDateTime newEndTime);
  List<Item> getActiveItemsList();
  Item saveItem(Item item);
  List<Item> getAuctionHistoryByUserId(Long userId);
}
