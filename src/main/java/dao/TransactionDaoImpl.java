package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionDaoImpl implements TransactionDao{
  @Override
  public void finalizeAuction(Long itemId, Long winnerId, BigDecimal finalPrice) {
    String updateItemSql = "UPDATE items SET status = ?, current_price = ?, winner_id = ? WHERE id = ?";
    String updateUserBalanceSql = "UPDATE users SET balance = balance - ? WHERE id = ? AND balance >= ?";

    try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
      conn.setAutoCommit(false);
      try {
        try (PreparedStatement itemStm = conn.prepareStatement(updateItemSql)) {
          itemStm.setString(1, "ENDED");
          itemStm.setBigDecimal(2, finalPrice);
          itemStm.setObject(3, winnerId);
          itemStm.setLong(4, itemId);
          itemStm.executeUpdate();
        }

        if (winnerId != null && finalPrice != null) {
          try (PreparedStatement userStm = conn.prepareStatement(updateUserBalanceSql)) {
            userStm.setBigDecimal(1, finalPrice);
            userStm.setLong(2, winnerId);
            userStm.setBigDecimal(3, finalPrice);
            int affectedRows = userStm.executeUpdate();
            if (affectedRows == 0) {
              throw new SQLException("Số dư của winner không đủ hoặc không tìm thấy user!");
            }
          }
        }
        conn.commit();
      } catch (Exception e) {
        conn.rollback();
        e.printStackTrace();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

