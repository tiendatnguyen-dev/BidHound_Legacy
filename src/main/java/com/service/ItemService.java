package com.service;

import com.dto.exceptions.ResourceNotFoundException;
import com.dto.request.CreateItemRequest;
import com.dto.response.ItemResponse;
import com.entities.Item;
import com.entities.User;
import com.repository.UserRepository;
import com.util.AuctionStatus;
import com.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
  private final ItemRepository itemDao;
  private final UserRepository userDao;

  public ItemService(ItemRepository itemDao, UserRepository userDao) {
    this.itemDao = itemDao;
    this.userDao = userDao;
  }

  public List<ItemResponse> getList() {
    List<Item> items = itemDao.findByAuctionStatus(AuctionStatus.ACTIVE);
    return getItemResponses(items);
  }

  public Item getDetail(Long itemId) {
    return itemDao.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item not found."));
  }

  @Transactional
  public Item createItem(CreateItemRequest createItemRequest) {
    User seller = userDao.findById(createItemRequest.sellerId()).orElseThrow(() -> new ResourceNotFoundException("Seller not found."));

    Item item = new Item(createItemRequest.id(), createItemRequest.title(),
            createItemRequest.currentPrice(), null, seller);
    return itemDao.save(item);
  }

  public List<ItemResponse> getUserAuctionHistory(Long userId) {
    List<Item> items = itemDao.findAuctionHistoryByUserId(userId);
    return getItemResponses(items);
  }

  @NonNull
  private List<ItemResponse> getItemResponses(List<Item> items) {
    List<ItemResponse> itemResponses = new ArrayList<>();
    for (Item item : items) {
      Long winnerId = item.getWinner() != null ? item.getWinner().getId() : null;
      itemResponses.add(new ItemResponse(item.getTitle(),
              item.getId(),item.getCurrentPrice(),item.getSeller().getId(),
              winnerId,item.getEndTime(),item.getAuctionStatus()));
    }
    return itemResponses;
  }
}
