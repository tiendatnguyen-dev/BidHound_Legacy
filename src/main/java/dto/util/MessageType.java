package dto.util;

/**
 * Phân loại message để xử lí trong ClientHandler.
 * Client -> Server :  JOIN_ROOM, LEAVE_ROOM, PLACE_BID
 * Server -> Client : JOIN_SUCCESS, LEAVE_SUCCESS, BID_BROADCAST, ERROR
 */
public enum MessageType {
  JOIN_ROOM, LEAVE_ROOM, PLACE_BID,
  JOIN_SUCCESS, LEAVE_SUCCESS, BID_BROADCAST, ERROR

}
