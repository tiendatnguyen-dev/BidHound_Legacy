package dto.mapper;

import java.math.BigDecimal;

/**
 * Class Data DTO, Từ Client -> Server khi Client đặt một bid.
 *
 * @param itemId id sản phẩm.
 * @param bidderId id client.
 * @param amount tổng số tiền bid.
 */
public record PlaceBidRequest(
        Long itemId,
        Long bidderId,
        BigDecimal amount
) {}
