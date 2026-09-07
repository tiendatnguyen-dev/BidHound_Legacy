package dto.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response trả về từ Server -> Client.
 *
 * @param bidId Id của bid.
 * @param itemId Id sản phẩm.
 * @param currentPrice giá hiện tại.
 * @param highestBidderId Id người vừa bid.
 * @param bidTime thời gian bid.
 * @param statusMessage trạng thái trả về.
 */
public record PlaceBidResponse(
        Long bidId,
        Long itemId,
        BigDecimal currentPrice,
        Long highestBidderId,
        LocalDateTime bidTime,
        String statusMessage
) {
}
