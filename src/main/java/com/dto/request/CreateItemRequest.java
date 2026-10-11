package com.dto.request;

import com.util.AuctionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateItemRequest(String title,
                                Long id,
                                BigDecimal currentPrice,
                                Long sellerId,
                                Long winnerId,
                                LocalDateTime endTime,
                                AuctionStatus auctionStatus) {
}
