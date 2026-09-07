package dao;

import dto.entities.Item;

import java.math.BigDecimal;

public interface ItemDao {
  Item findById(Long id);
  void updateCurrentPriceAndWinner(Long itemId, BigDecimal price, Long winnerId);
  void updateStatus(Long itemId, String status);
}
