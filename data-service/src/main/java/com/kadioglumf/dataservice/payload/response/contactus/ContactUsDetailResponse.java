package com.kadioglumf.dataservice.payload.response.contactus;

import com.kadioglumf.dataservice.converter.GenderConverter;
import com.kadioglumf.dataservice.enums.ContactUsStatus;
import com.kadioglumf.dataservice.enums.Gender;
import com.kadioglumf.dataservice.payload.response.ActivityItemResponse;
import com.kadioglumf.dataservice.payload.response.CommentItemResponse;
import jakarta.persistence.Convert;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class ContactUsDetailResponse {
  private Date creationDate;
  private Boolean isRegistered;
  private String contactRequestId;
  private ContactUsStatus status;
  private String name;
  private String surname;
  private String email;
  private String phone;
  private String subject;
  private String message;

  @Convert(converter = GenderConverter.class)
  private Gender gender;

  private List<CommentItemResponse> comments = new ArrayList<>();
  private List<ActivityItemResponse> activities = new ArrayList<>();
  private String acceptLanguage;
  private String originPath;
}
