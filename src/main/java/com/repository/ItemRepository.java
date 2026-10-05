package com.repository;

import com.entities.Item;
import com.util.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
  @Query("SELECT DISTINCT i FROM Item i JOIN i.bidHistory b WHERE b.user.id = :userId")
  List<Item> findAuctionHistoryByUserId(@Param("userId") Long userId);

  List<Item> findByAuctionStatus(AuctionStatus status);
}
