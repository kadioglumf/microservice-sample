package com.kadioglumf.dataservice.payload.response;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadFilesResponse {
  private String fileName;
  private String path;
  private Date creationDate;
  private Date lastUpdateDate;
  private Long size;
  private String type;
  private String publicUrl;
}
