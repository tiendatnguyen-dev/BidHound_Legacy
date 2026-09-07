package dao;

import dto.entities.User;

import java.math.BigDecimal;

public interface UserDao {
  User findUserById(Long userId);
  boolean updateUserBalance(Long userId, BigDecimal newBalance);
}
