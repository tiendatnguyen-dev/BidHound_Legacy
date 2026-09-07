package dto.mapper;

import dto.entities.Bid;

import java.time.LocalDateTime;

public class BidMapper {

  /**
   * Chuyển đổi từ một Request sang thành 1 Object.
   *
   * @param request Request gửi về từ Server.
   * @return 1 Object Bid.
   */
  public static Bid toEntity(PlaceBidRequest request) {
    return new Bid(0L, request.itemId(), request.bidderId(),
            request.amount(), LocalDateTime.now());
  }

  /**
   * Từ Object sẵn có, trả về Response.
   *
   * @param bid bid hiện tại.
   * @param statusMessage trạng thái.
   * @return 1 Response Cho Server.
   */
  public static PlaceBidResponse toDTO(Bid bid, String statusMessage) {
    return new PlaceBidResponse(bid.getId(),bid.getItemId(),
            bid.getAmount(),bid.getBidderId(),bid.getCreatedAt(), statusMessage);
  }
}
