package dto.util;

/**
 * Time format cho AuctionRoomController, định dạng hiện tại: HH:MM:SS.
 */
public class TimeCounter {
  public static String timeFormatter(int remainingSeconds) {
    int hours = remainingSeconds / 3600;
    int minutes = (remainingSeconds % 3600) / 60;
    int seconds = remainingSeconds % 60;
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }
}
