package com.dao;

import com.dto.entities.Bid;
import com.dto.entities.Item;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemDaoImpl implements ItemDao {
  private final BidDao bidDao;
  private final DatabaseConnection databaseConnection;

  public ItemDaoImpl(BidDao bidDao, DatabaseConnection databaseConnection) {
    this.bidDao = bidDao;
    this.databaseConnection = databaseConnection;
  }

  @Override
  public Item findById(Long id) {
    Item item = new Item();
    String sql = "SELECT id,title,seller_id,winner_id,current_price,status,end_time FROM items WHERE id = ?";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
      preparedStatement.setLong(1, id);
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
        for (Bid bid : bidDao.findByItemId(itemId)) {
          item.addBid(bid);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return item;
  }

  @Override
  public void updateCurrentPriceAndWinner(Long itemId, BigDecimal price, Long winnerId) {
    String sql = "UPDATE items SET current_price = ?, winner_id = ? WHERE id = ?";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
      preparedStatement.setBigDecimal(1, price);
      preparedStatement.setLong(2, winnerId);
      preparedStatement.setLong(3, itemId);
      preparedStatement.executeUpdate();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateStatus(Long itemId, String status) {
    String sql = "UPDATE items SET status = ? WHERE id = ?";
    try (Connection conn = databaseConnection.getConnection();
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
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setString(1, newEndTime.toString());
      stm.setTimestamp(2, Timestamp.valueOf(newEndTime));
      stm.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Item> getActiveItemsList() {
    String sql = "SELECT id,title,seller_id,winner_id,current_price,status,end_time FROM items WHERE status = ?";
    List<Item> items = new ArrayList<Item>();
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
      preparedStatement.setString(1, "ACTIVE");
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        Item item = new Item();
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
        for (Bid bid : bidDao.findByItemId(itemId)) {
          item.addBid(bid);
        }
        items.add(item);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return items;
  }

  @Override
  public Item saveItem(Item item) {
    String sql = "INSERT INTO items (title, seller_id, current_price, status, end_time) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      pstm.setString(1, item.getTitle());
      pstm.setLong(2, item.getSellerId());
      pstm.setBigDecimal(3, item.getCurrentPrice());
      pstm.setString(4, "ACTIVE");
      pstm.setTimestamp(5, Timestamp.valueOf(item.getEndTime()));
      pstm.executeUpdate();
      try (ResultSet rs = pstm.getGeneratedKeys()) {
        if (rs.next()) {
          item.setId(rs.getLong(1));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return item;
  }

  @Override
  public List<Item> getAuctionHistoryByUserId(Long userId) {
    List<Item> items = new ArrayList<>();
    String sql = "SELECT DISTINCT i.id, i.title, i.seller_id, i.winner_id, i.current_price, i.status, i.end_time " +
            "FROM items i " +
            "JOIN bids b ON i.id = b.item_id " +
            "WHERE b.user_id = ?";

    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement pstm = conn.prepareStatement(sql)) {

      pstm.setLong(1, userId);
      ResultSet rs = pstm.executeQuery();

      while (rs.next()) {
        Item item = new Item();
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
        for (Bid bid : bidDao.findByItemId(itemId)) {
          item.addBid(bid);
        }
        items.add(item);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return items;
  }
}
