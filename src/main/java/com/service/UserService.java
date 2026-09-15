package com.service;

import com.dto.entities.User;
import com.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
  private final UserRepository userDao;

  public UserService(UserRepository userDao) {
    this.userDao = userDao;
  }

  public User authenticateUser(Long userId, String username) {
    User currentUser = userDao.findById(userId).orElse(null);
    if (currentUser != null && currentUser.getUsername().equals(username)) {
      return currentUser;
    }
    return null;
  }

  @Transactional
  public User registerUser(User user) {
    if (userDao.findByUsername(user.getUsername()).isPresent()) {
      return null;
    }
    else {
      return userDao.save(user);
    }
  }

  @Transactional
  public void topUpBalance(User user, BigDecimal newBalance) {
    userDao.findById(user.getId()).ifPresent(userdb -> { userdb.setBalance(userdb.getBalance().add(newBalance));
    });
  }

}
