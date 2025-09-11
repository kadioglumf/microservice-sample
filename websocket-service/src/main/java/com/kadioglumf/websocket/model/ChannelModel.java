package com.kadioglumf.websocket.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "channel")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelModel extends DeviceDetailedAbstractModel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "channel_id_generator")
  @SequenceGenerator(
      name = "channel_id_generator",
      sequenceName = "seq_channel_id_generator",
      allocationSize = 1)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "channel_roles", joinColumns = @JoinColumn(name = "channel_id"))
  private Set<String> roles;

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<UserChannelPreferencesModel> userChannelPreferences = new ArrayList<>();
}
