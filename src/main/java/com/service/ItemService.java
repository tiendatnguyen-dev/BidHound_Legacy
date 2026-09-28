package com.service;

import com.dto.entities.Item;
import com.dto.util.AuctionStatus;
import com.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
  private final ItemRepository itemDao;

  public ItemService(ItemRepository itemDao) {
    this.itemDao = itemDao;
  }

  public List<Item> getList() {
    return itemDao.findByAuctionStatus(AuctionStatus.ACTIVE);
  }

  public Item getDetail(Long itemId) {
    return itemDao.findById(itemId).orElse(null);
  }

  @Transactional
  public Item createItem(Item item) {
    return itemDao.save(item);
  }

  public List<Item> getUserAuctionHistory(Long userId) {
    return itemDao.findAuctionHistoryByUserId(userId);
  }
}
