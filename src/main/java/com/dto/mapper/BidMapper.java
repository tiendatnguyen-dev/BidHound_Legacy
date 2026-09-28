package com.dto.mapper;

import com.dto.entities.Bid;
import com.dto.entities.Item;
import com.dto.entities.User;

import java.time.LocalDateTime;

public class BidMapper {

  /**
   * Chuyển đổi từ một Request sang thành 1 Object.
   *
   * @param request Request gửi về từ Server.
   * @return 1 Object Bid.
   */
  public static Bid toEntity(PlaceBidRequest request, Item item, User bidder) {
    return new Bid(
            null,
            item,
            bidder,
            request.amount(),
            LocalDateTime.now()
    );
  }

  /**
   * Từ Object sẵn có, trả về Response.
   *
   * @param bid bid hiện tại.
   * @param statusMessage trạng thái.
   * @return 1 Response Cho Server.
   */
  public static PlaceBidResponse toDTO(Bid bid, String statusMessage) {
    return new PlaceBidResponse(bid.getId(),bid.getItem().getId(),
            bid.getAmount(),bid.getUser().getId(),bid.getCreatedAt(), statusMessage);
  }
}
