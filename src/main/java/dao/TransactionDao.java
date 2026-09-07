package dao;

import java.math.BigDecimal;

public interface TransactionDao {
  void finalizeAuction(Long itemId, Long winnerId, BigDecimal finalPrice);
}
