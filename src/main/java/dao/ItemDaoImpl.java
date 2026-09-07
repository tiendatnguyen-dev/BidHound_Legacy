package dao;

import dto.entities.Item;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ItemDaoImpl implements ItemDao{
  @Override
  public Item findById(Long id) {
    Item item = new Item();
    String sql = "SELECT id,title,seller_id,winner_id,current_price,status,end_time FROM items WHERE id = ?";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
      preparedStatement.setLong(1,id);
      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        Long itemId = rs.getLong("id");
        String title = rs.getString("title");
        Long sellerId = rs.getLong("seller_id");
        Long winnerId = rs.getLong("winner_id");
        BigDecimal currPrice = rs.getBigDecimal("current_price");
        String status = rs.getString("status");
        LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();
        item.setId(itemId);
        item.setTitle(title);
        item.setSellerId(sellerId);
        item.setWinnerId(winnerId);
        item.setCurrentPrice(currPrice);
        item.setStatus(status);
        item.setEndTime(endTime);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return item;
  }

  @Override
  public void updateCurrentPriceAndWinner(Long itemId, BigDecimal price, Long winnerId) {
    String sql = "UPDATE items SET current_price = ?, winner_id = ? WHERE id = ?";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
      preparedStatement.setBigDecimal(1, price);
      preparedStatement.setLong(2,winnerId);
      preparedStatement.setLong(3,itemId);
      preparedStatement.executeUpdate();

    } catch (Exception e) {
       e.printStackTrace();
    }
  }

  @Override
  public void updateStatus(Long itemId, String status) {
    String sql = "UPDATE items SET status = ? WHERE id = ?";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
      preparedStatement.setString(1, status);
      preparedStatement.setLong(2, itemId);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateEndTime(Long itemId, LocalDateTime newEndTime) {
    String sql = "UPDATE items SET end_time = ? WHERE id = ?";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setString(1, newEndTime.toString());
      stm.setTimestamp(2, Timestamp.valueOf(newEndTime));
      stm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
