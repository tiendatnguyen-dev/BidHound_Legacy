package com.network.api;

import com.entities.Item;
import com.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final ItemService itemService;

  public UserController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping("/{userId}/history")
  public ResponseEntity<?> getAuctionHistory(@PathVariable ("userId") Long userId) {
    List<Item> history = itemService.getUserAuctionHistory(userId);
    if (history != null) {
      return ResponseEntity.ok(history);
    }
    else {
      return ResponseEntity.badRequest().body(Map.of("message", "User not found."));
    }
  }
}
