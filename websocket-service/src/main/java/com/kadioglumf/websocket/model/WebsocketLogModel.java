package com.kadioglumf.websocket.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@Entity
@Table(name = "websocket_exception_logs")
@EqualsAndHashCode(callSuper = true)
public class WebsocketLogModel extends DeviceDetailedAbstractModel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exception_logs_id_generator")
  @SequenceGenerator(
      name = "exception_logs_id_generator",
      sequenceName = "seq_exception_logs_id_generator",
      allocationSize = 1)
  private Long id;

  private String email;

  @Lob
  @JdbcTypeCode(SqlTypes.LONGVARCHAR)
  private String request;

  @Lob
  @JdbcTypeCode(SqlTypes.LONGVARCHAR)
  private String exceptionMessage;

  @Lob
  @JdbcTypeCode(SqlTypes.LONGVARCHAR)
  private String exceptionStackTrace;

  private String className;
  private String methodName;
  private int errorCode;
  private String errorType;
  private int httpStatusCode;
  private String transactionId;
}
