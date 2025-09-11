package com.kadioglumf.websocket.repository.log;

import com.kadioglumf.websocket.model.WebsocketLogModel;

public interface WebsocketLogRepository {
  void save(WebsocketLogModel logModel);
}
