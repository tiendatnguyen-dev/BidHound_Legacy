package com.dao;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class DatabaseConnection {
  private static String URL;
  private static String PASSWORD;
  private static String USER;

  public DatabaseConnection() {
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

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }
}