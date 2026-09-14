package com.dao;

import com.dto.entities.User;

import java.math.BigDecimal;

public interface UserDao {
  User findUserById(Long userId);
  User findUserByUsername(String username);
  boolean updateUserBalance(Long userId, BigDecimal newBalance);
  User createUser(User user);
}
