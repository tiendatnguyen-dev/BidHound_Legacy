package com.network.api;


import com.dto.ApiResponse;
import com.dto.request.CreateItemRequest;
import com.dto.response.ItemResponse;
import com.entities.Item;
import com.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
  private final ItemService itemService;

  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ItemResponse>> getDetail(@PathVariable("id") Long itemId) {
    Item detailedItem = itemService.getDetail(itemId);
    Long winnerId = detailedItem.getWinner() != null ? detailedItem.getWinner().getId() : null;

      ItemResponse getItemResponse = new ItemResponse(detailedItem.getTitle(),
              detailedItem.getId(), detailedItem.getCurrentPrice(), detailedItem.getSeller().getId(),
              winnerId, detailedItem.getEndTime(), detailedItem.getAuctionStatus());

      return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(getItemResponse));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ItemResponse>>> getItemList() {
    List<ItemResponse> items = itemService.getList();
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(items));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Long>> createItem(@RequestBody CreateItemRequest createItemRequest) {
    Item item = itemService.createItem(createItemRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(item.getId(), "Item created"));
  }
}
