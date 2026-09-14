package com.service;

import com.dao.BidDaoImpl;
import com.dao.DatabaseConnection;
import com.dao.ItemDao;
import com.dao.ItemDaoImpl;
import com.dto.entities.Item;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
  private final ItemDao itemDao;

  public ItemService() {
    DatabaseConnection db = new DatabaseConnection();
    this.itemDao = new ItemDaoImpl(new BidDaoImpl(db), db);
  }

  public ItemService(ItemDao itemDao) {
    this.itemDao = itemDao;
  }

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
