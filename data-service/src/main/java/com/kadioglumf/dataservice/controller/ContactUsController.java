package com.kadioglumf.dataservice.controller;

import com.kadioglumf.dataservice.core.enums.RoleTypeEnum;
import com.kadioglumf.dataservice.core.secure.Secure;
import com.kadioglumf.dataservice.enums.SearchRoleType;
import com.kadioglumf.dataservice.payload.request.contactus.CreateContactUsRequest;
import com.kadioglumf.dataservice.payload.request.search.SearchRequest;
import com.kadioglumf.dataservice.payload.response.NewCommentResponse;
import com.kadioglumf.dataservice.payload.response.contactus.ContactUsDetailResponse;
import com.kadioglumf.dataservice.payload.response.contactus.CreateContactUsCommentRequest;
import com.kadioglumf.dataservice.payload.response.contactus.CreateContactUsResponse;
import com.kadioglumf.dataservice.payload.response.contactus.GetAdminContactUsResponse;
import com.kadioglumf.dataservice.service.CaptchaService;
import com.kadioglumf.dataservice.service.ContactUsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Contact Us Controller", description = "Contact Us Controller documentation")
@RestController
@RequestMapping("/contact-us")
@RequiredArgsConstructor
@Validated
public class ContactUsController {

  private final ContactUsService contactUsService;
  private final CaptchaService captchaService;

  @PostMapping("/create")
  @Operation(summary = "Create Contact Us")
  public ResponseEntity<CreateContactUsResponse> createContactUs(
      @Valid @RequestBody CreateContactUsRequest request) {
    captchaService.verify(request.getRecaptchaResponse());
    return ResponseEntity.ok(contactUsService.createContactUs(request));
  }

  @PostMapping("/admin/getContactRequests")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  @Operation(summary = "Get Contact Requests")
  public ResponseEntity<Page<GetAdminContactUsResponse>> getContactRequestsListForAdmin(
      @Valid @RequestBody SearchRequest request) {
    return ResponseEntity.ok(
        contactUsService.getContactRequestsForAdmin(request, SearchRoleType.ADMIN));
  }

  @GetMapping("/admin/getContactRequestDetail")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  @Operation(summary = "Get Contact Request Detail")
  public ResponseEntity<ContactUsDetailResponse> getContactUsDetailForAdmin(
        @RequestParam String contactRequestId) {
    return ResponseEntity.ok(
        contactUsService.getContactRequestDetail(contactRequestId, SearchRoleType.ADMIN));
  }

  @PostMapping("/admin/newComment")
  @Operation(summary = "Create New Comment For Only Admin")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<NewCommentResponse> newCommentForAdmin(
      @Valid @RequestBody CreateContactUsCommentRequest request) {
    return new ResponseEntity<>(
        contactUsService.newComment(request, SearchRoleType.ADMIN), HttpStatus.OK);
  }

  @DeleteMapping(value = "/admin/deleteContactUs")
  @Operation(summary = "Delete Contact Us")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<?> deleteContactUs(
      @RequestParam("contactRequestId") String contactRequestId) {
    contactUsService.deleteContactUsRequest(contactRequestId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
