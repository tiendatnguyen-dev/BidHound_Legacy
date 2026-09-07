package dto.util;

/**
 * Data class để gửi về ClientHandler, giúp phân loại và xử lí.
 * @param type loại message, xem trong MessageType.
 * @param payload chuỗi Json chứa thông tin chưa được chuẩn hóa từ BidMapper về.
 */
public record MessageEnvelop(
        MessageType type, String payload
) {}
