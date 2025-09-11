package com.kadioglumf.dataservice.payload.response;

import java.util.Date;
import lombok.Data;

@Data
public class CommentItemResponse {
  private AuthorUserResponse author;
  private String commentId;
  private String message;
  private Date creationDate;
}
