package com.dto.response;

import com.entities.User;
import com.util.AuctionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemResponse(String title,
                           Long id,
                           BigDecimal currentPrice,
                           Long sellerId,
                           Long winnerId,
                           LocalDateTime endTime,
                           AuctionStatus auctionStatus) {
}
