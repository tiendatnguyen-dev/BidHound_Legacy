package dao;

import dto.entities.Bid;
import java.util.List;

public interface BidDao {
  void save(Bid bid);
  List<Bid> findByItemId(Long itemId);
}
