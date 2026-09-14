package com.dao;

import com.dto.entities.Bid;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BidDaoImpl implements BidDao{

  private final DatabaseConnection databaseConnection;

  public BidDaoImpl(DatabaseConnection databaseConnection) {
    this.databaseConnection = databaseConnection;
  }

  @Override
  public void save(Bid bid) {
    String sql = "INSERT INTO bids (item_id, bidder_id, amount, created_at) VALUES (?, ?, ?, NOW())";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
      preparedStatement.setLong(1, bid.getItemId());
      preparedStatement.setLong(2, bid.getBidderId());
      preparedStatement.setBigDecimal(3, bid.getAmount());
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Bid> findByItemId(Long itemId) {
    List<Bid> Bids = new ArrayList<>();
    String sql = "SELECT * FROM bids WHERE item_id = ? ORDER BY created_at DESC";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
      preparedStatement.setLong(1, itemId);
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        Long id = rs.getLong("id");
        BigDecimal amount = rs.getBigDecimal("amount");
        Long bidderId = rs.getLong("bidder_id");
        Timestamp created = rs.getTimestamp("created_at");
        LocalDateTime localDateTime = created.toLocalDateTime();
        Bid bid = new Bid(id,itemId, bidderId,amount, localDateTime);
        Bids.add(bid);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Bids;
  }
}
