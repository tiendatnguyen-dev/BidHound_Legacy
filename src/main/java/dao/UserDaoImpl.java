package dao;

import dto.entities.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao{
  @Override
  public User findUserById(Long userId) {
    String sql = "SELECT id,username,email,balance FROM users WHERE id = ?";
    User user = new User();
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement stm = conn.prepareStatement(sql);) {
      stm.setLong(1, userId);
      ResultSet rs = stm.executeQuery();
        if (rs.next()) {
          Long id = rs.getLong("id");
          String username = rs.getString("username");
          String email = rs.getString("email");
          BigDecimal balance = rs.getBigDecimal("balance");

          user.setId(id);
          user.setUsername(username);
          user.setBalance(balance);
          user.setEmail(email);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  @Override
  public boolean updateUserBalance(Long userId, BigDecimal newBalance) {
    String sql = "UPDATE users SET balance = ? WHERE id = ?";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
}
