package com.dto.request;

import com.entities.User;
import com.util.AuctionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateItemRequest(String title,
                                Long id,
                                BigDecimal currentPrice,
                                User seller,
                                User winner,
                                LocalDateTime endTime,
                                AuctionStatus auctionStatus) {
}
