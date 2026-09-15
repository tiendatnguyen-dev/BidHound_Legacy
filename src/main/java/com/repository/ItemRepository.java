package com.repository;

import com.dto.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
  @Query("SELECT DISTINCT i FROM Item i, Bid b WHERE i.id = b.itemId AND b.bidderId = :userId")
  List<Item> findAuctionHistoryByUserId(@Param("userId") Long userId);

  List<Item> findByStatus(String status);
}
