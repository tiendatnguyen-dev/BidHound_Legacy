package com.service;

import com.dto.exceptions.AuthorizedException;
import com.dto.exceptions.BadRequestException;
import com.entities.User;
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
    User currentUser = userDao.findById(userId).orElseThrow(() -> new AuthorizedException("Not authorized."));
    if (currentUser.getUsername().equals(username)) {
      return currentUser;
    }
    throw new AuthorizedException("Not authorized.");
  }

  @Transactional
  public User registerUser(String username) {
    if (userDao.findByUsername(username).isPresent()) {
      throw new BadRequestException("Username already exists.");
    }
    else {
      User user = new User();
      user.setUsername(username);
      return userDao.save(user);
    }
  }

  @Transactional
  public void topUpBalance(User user, BigDecimal newBalance) {
    userDao.findById(user.getId()).ifPresent(userdb -> { userdb.setBalance(userdb.getBalance().add(newBalance));
    });
  }

}
