package dao;

import dto.entities.Item;
import dto.entities.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {
  User findUserById(Long userId);
  boolean updateUserBalance(Long userId, BigDecimal newBalance);
}
