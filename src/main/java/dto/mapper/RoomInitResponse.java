package dto.mapper;

import dto.entities.Bid;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO Class, giúp cho Clients mới vào Room lấy được data, bọc trong MessageEnvelop.
 *
 * @param itemId itemId phòng hiện tại.
 * @param title tên sản phẩm.
 * @param sellerId tên người bán.
 * @param currentPrice giá hiện tại.
 * @param remainingSeconds thời gian còn lại của auction.
 * @param status trạng thái phiên.
 * @param bids lịch sử các bid cũ.
 */
public record RoomInitResponse(
        Long itemId,
        String title,
        Long sellerId,
        BigDecimal currentPrice,
        int remainingSeconds,
        String status,
        List<Bid> bids
        ) {}
