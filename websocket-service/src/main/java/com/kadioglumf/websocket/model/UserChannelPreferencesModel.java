package com.kadioglumf.websocket.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_channel_preferences")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChannelPreferencesModel extends AbstractModel {
  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "channel_preferences_id_generator")
  @SequenceGenerator(
      name = "channel_preferences_id_generator",
      sequenceName = "seq_channel_preferences_id_generator",
      allocationSize = 1)
  private Long id;

  private Long userId;
  private boolean isSubscribed;

  @ManyToOne private ChannelModel channel;
}
