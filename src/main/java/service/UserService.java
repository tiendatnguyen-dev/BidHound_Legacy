package service;

import dao.UserDao;
import dao.UserDaoImpl;
import dto.entities.User;

public class UserService {
  UserDao userDao = new UserDaoImpl();
  public boolean authenticateUser(Long userId, String username) {
    User currentUser = userDao.findUserById(userId);
    return currentUser.getUsername().equals(username);
  }

  public boolean registerUser(String username) {
    return true;
  }
}
