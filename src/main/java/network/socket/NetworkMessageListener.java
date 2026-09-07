package network.socket;

import dto.util.MessageEnvelop;

/**
 * Chịu trách nhiệm update UI cho người dùng (ClientSide).
 */
public interface NetworkMessageListener {
  void onMessageReceived(MessageEnvelop envelope);
}

