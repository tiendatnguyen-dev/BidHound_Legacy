package service;

import dao.UserDao;
import dao.UserDaoImpl;
import dto.entities.User;

import java.math.BigDecimal;

public class UserService {
  UserDao userDao = new UserDaoImpl();
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
