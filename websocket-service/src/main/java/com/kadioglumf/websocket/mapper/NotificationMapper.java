package com.kadioglumf.websocket.mapper;

import com.kadioglumf.websocket.model.NotificationModel;
import com.kadioglumf.websocket.payload.response.notification.NotificationResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  List<NotificationResponse> toNotificationResponseList(List<NotificationModel> models);
}
