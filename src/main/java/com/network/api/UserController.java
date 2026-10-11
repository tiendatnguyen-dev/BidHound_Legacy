package com.network.api;

import com.dto.ApiResponse;
import com.dto.request.ItemRequest;
import com.dto.response.ItemResponse;
import com.entities.Item;
import com.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final ItemService itemService;

  public UserController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping("/{userId}/history")
  public ResponseEntity<ApiResponse<List<ItemResponse>>> getAuctionHistory(@PathVariable ("userId") Long userId) {
    List<ItemResponse> history = itemService.getUserAuctionHistory(userId);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(history));
  }
}
