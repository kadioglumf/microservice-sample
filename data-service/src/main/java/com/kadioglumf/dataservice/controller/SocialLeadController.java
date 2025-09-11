package com.kadioglumf.dataservice.controller;

import com.kadioglumf.dataservice.core.enums.RoleTypeEnum;
import com.kadioglumf.dataservice.core.secure.Secure;
import com.kadioglumf.dataservice.payload.request.search.SearchRequest;
import com.kadioglumf.dataservice.payload.request.sociallead.*;
import com.kadioglumf.dataservice.payload.request.sociallead.CreateSocialLeadCommentRequest;
import com.kadioglumf.dataservice.payload.response.NewCommentResponse;
import com.kadioglumf.dataservice.payload.response.sociallead.AdminSocialLeadDetailResponse;
import com.kadioglumf.dataservice.payload.response.sociallead.AdminSocialLeadListResponse;
import com.kadioglumf.dataservice.service.SocialLeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Social Lead Controller", description = "Social Lead Controller documentation")
@RestController
@RequestMapping("/socialLead")
@RequiredArgsConstructor
@Validated
public class SocialLeadController {

  private final SocialLeadService socialLeadService;

  @PostMapping(value = "/admin/import")
  @Operation(summary = "Import Social Lead File")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<Void> importSocialLead(@RequestPart MultipartFile file) {
    socialLeadService.importFile(file);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/admin/getSocialLeads")
  @Operation(summary = "Get Social Lead List For Only Admin")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<Page<AdminSocialLeadListResponse>> getSocialLeadsForAdmin(
      @Valid @RequestBody SearchRequest request) {
    return new ResponseEntity<>(socialLeadService.listingForAdmin(request), HttpStatus.OK);
  }

  @GetMapping("/admin/getSocialLeadDetail")
  @Operation(summary = "Get Social Lead Detail For Only Admin")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<AdminSocialLeadDetailResponse> getSocialLeadDetailForAdmin(
      @RequestParam Long id) {
    return ResponseEntity.ok().body(socialLeadService.getSocialLeadDetailForAdmin(id));
  }

  @PostMapping("/admin/newComment")
  @Operation(summary = "Create New Comment For Only Admin")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<NewCommentResponse> newCommentForAdmin(
      @Valid @RequestBody CreateSocialLeadCommentRequest request) {
    return new ResponseEntity<>(socialLeadService.newComment(request), HttpStatus.OK);
  }

  @DeleteMapping(value = "/admin/delete")
  @Operation(summary = "Delete Social Lead")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<?> delete(@RequestParam Long id) {
    socialLeadService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
