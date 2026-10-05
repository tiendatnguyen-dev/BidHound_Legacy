package com.network.api;


import com.dto.request.CreateItemRequest;
import com.dto.response.ItemResponse;
import com.entities.Item;
import com.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {
  private final ItemService itemService;

  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getDetail(@PathVariable("id") Long itemId) {
    Item detailedItem = itemService.getDetail(itemId);

    if (detailedItem != null) {
      ItemResponse getItemResponse = new ItemResponse(detailedItem.getTitle(),
              detailedItem.getId(), detailedItem.getCurrentPrice(), detailedItem.getSeller(),
              detailedItem.getWinner(), detailedItem.getEndTime(), detailedItem.getAuctionStatus());

      return ResponseEntity.ok(getItemResponse);
    }
    else {
      return ResponseEntity.badRequest().body(Map.of("message","Item not found"));
    }

  }

  @GetMapping
  public ResponseEntity<?> getItemList() {
    List<Item> items = itemService.getList();
    if (items != null) {
      return ResponseEntity.ok(items);
    } else {
      return ResponseEntity.badRequest().body(Map.of("message","An error occured with the server."));
    }
  }

  @PostMapping
  public ResponseEntity<?> createItem(@RequestBody CreateItemRequest createItemRequest) {
    Item item = itemService.createItem(createItemRequest);
    if (item != null) {
      return ResponseEntity.ok(item);
    } else {
      return ResponseEntity.badRequest().body(Map.of("message", "An error occured with the server database."));
    }
  }
}
