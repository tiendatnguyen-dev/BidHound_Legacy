package com.dao;

import com.dto.entities.User;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class UserDaoImpl implements UserDao {
  private final DatabaseConnection databaseConnection;

  public UserDaoImpl(DatabaseConnection databaseConnection) {
    this.databaseConnection = databaseConnection;
  }

  @Override
  public User findUserById(Long userId) {
    String sql = "SELECT id,username,email,balance FROM users WHERE id = ?";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setLong(1, userId);
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setBalance(rs.getBigDecimal("balance"));
        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public User findUserByUsername(String username) {
    String sql = "SELECT id, username, email, balance FROM users WHERE username = ?";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setString(1, username);
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setBalance(rs.getBigDecimal("balance"));
        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean updateUserBalance(Long userId, BigDecimal newBalance) {
    String sql = "UPDATE users SET balance = ? WHERE id = ?";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {
      stm.setBigDecimal(1, newBalance);
      stm.setLong(2, userId);
      int rowsAffected = stm.executeUpdate();
      if (rowsAffected > 0) {
        System.out.println("Updated successfully.");
        return true;
      } else {
        System.out.println("Update failed.");
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public User createUser(User user) {
    String sql = "INSERT INTO users (username, email, balance) VALUES (?, ?, ?)";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement ptmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ptmt.setString(1, user.getUsername());
      ptmt.setString(2, user.getEmail());
      ptmt.setBigDecimal(3, BigDecimal.ZERO);
      int rowsAffected = ptmt.executeUpdate();
      if (rowsAffected > 0) {
        ResultSet rs = ptmt.getGeneratedKeys();
        if (rs.next()) {
          Long newId = rs.getLong(1);
          user.setId(newId);
        }
        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
