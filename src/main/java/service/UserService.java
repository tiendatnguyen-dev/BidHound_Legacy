package service;

import dao.UserDao;
import dao.UserDaoImpl;
import dto.entities.User;

public class UserService {
  UserDao userDao = new UserDaoImpl();
  public User authenticateUser(Long userId, String username) {
    User currentUser = userDao.findUserById(userId);
    if (currentUser != null && currentUser.getUsername().equals(username)) {
      return currentUser;
    }
    return null;
  }

  public boolean registerUser(String username) {
    return true;
  }
}
