package dao;

import dto.entities.Item;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ItemDao {
  Item findById(Long id);
  void updateCurrentPriceAndWinner(Long itemId, BigDecimal price, Long winnerId);
  void updateStatus(Long itemId, String status);
  void updateEndTime(Long itemId, LocalDateTime newEndTime);
}
