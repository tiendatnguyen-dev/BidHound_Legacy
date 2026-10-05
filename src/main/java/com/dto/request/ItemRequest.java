package com.dto.request;

public class ItemRequest {
  private Long itemId;

  public ItemRequest(Long itemId) {
    this.itemId = itemId;
  }

  public Long getItemId() {
    return itemId;
  }
}
