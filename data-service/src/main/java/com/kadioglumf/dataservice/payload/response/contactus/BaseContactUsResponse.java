package com.kadioglumf.dataservice.payload.response.contactus;

import com.kadioglumf.dataservice.enums.ContactUsStatus;
import java.util.Date;
import lombok.Data;

@Data
public class BaseContactUsResponse {
  private Date creationDate;
  private String contactRequestId;
  private ContactUsStatus status;
  private String name;
  private String surname;
  private String email;
  private String phone;
  private String subject;
}
