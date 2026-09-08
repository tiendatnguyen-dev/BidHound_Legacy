package service;

import dao.ItemDao;
import dao.ItemDaoImpl;
import dto.entities.Item;

import java.util.List;

public class ItemService {
  ItemDao itemDao = new ItemDaoImpl();
  public List<Item> getList() {
    return itemDao.getActiveItemsList();
  }

  public Item getDetail(Long itemId) {
    return itemDao.findById(itemId);
  }

  public Item createItem(Item item) {
    itemDao.saveItem(item);
    return item;
  }

  public List<Item> getUserAuctionHistory(Long userId) {
    return itemDao.getAuctionHistoryByUserId(userId);
  }
}
