package service;

import com.sun.net.httpserver.HttpExchange;
import dao.ItemDao;
import dao.ItemDaoImpl;
import dto.entities.Item;
import dto.util.HttpUtils;

import java.util.List;

public class ItemService {
  ItemDao itemDao = new ItemDaoImpl();
  public List<Item> getList() {
    return itemDao.getActiveItemsList();
  }

  public Item getDetail(Long itemId) {
    return itemDao.findById(itemId);
  }
}
