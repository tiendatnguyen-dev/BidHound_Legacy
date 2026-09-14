package com.service;

import com.dao.DatabaseConnection;
import com.dao.UserDao;
import com.dao.UserDaoImpl;
import com.dto.entities.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
  private final UserDao userDao;

  public UserService() {
    this(new UserDaoImpl(new DatabaseConnection()));
  }

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public User authenticateUser(Long userId, String username) {
    User currentUser = userDao.findUserById(userId);
    if (currentUser != null && currentUser.getUsername().equals(username)) {
      return currentUser;
    }
    return null;
  }

  public User registerUser(User user) {
    if (userDao.findUserByUsername(user.getUsername()) != null) {
      return null;
    }
    else {
      return userDao.createUser(user);
    }
  }

  public void topUpBalance(User user, BigDecimal newBalance) {
    userDao.updateUserBalance(user.getId(),newBalance.add(user.getBalance()));
  }
}
