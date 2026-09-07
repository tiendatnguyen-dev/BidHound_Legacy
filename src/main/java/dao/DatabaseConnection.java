package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
  private static String URL;
  private static String PASSWORD;
  private static String USER;
  private static volatile DatabaseConnection instance;

  private DatabaseConnection() {
    Properties prop = new Properties();
    try (InputStream inputStream = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
      if (inputStream == null) {
        System.out.println("db.properties không tồn tại.");
      } else {
        prop.load(inputStream);
        URL = prop.getProperty("db.url");
        PASSWORD = prop.getProperty("db.password");
        USER = prop.getProperty("db.user");
        System.out.println("Nạp cấu hình thành công.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static DatabaseConnection getInstance() {
    DatabaseConnection databaseConnection = instance;
    if (databaseConnection == null) {
      synchronized (DatabaseConnection.class) {
        databaseConnection = instance;
        if (databaseConnection == null) {
          databaseConnection = instance = new DatabaseConnection();
        }
      }
    }
    return instance;
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }
}