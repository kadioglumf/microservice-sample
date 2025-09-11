package com.kadioglumf.dataservice.mapper;

import com.kadioglumf.dataservice.model.sociallead.SocialLeadActivityModel;
import com.kadioglumf.dataservice.model.sociallead.SocialLeadCommentModel;
import com.kadioglumf.dataservice.model.sociallead.SocialLeadModel;
import com.kadioglumf.dataservice.payload.request.sociallead.SocialLeadImportRequest;
import com.kadioglumf.dataservice.payload.response.*;
import com.kadioglumf.dataservice.payload.response.sociallead.AdminSocialLeadDetailResponse;
import com.kadioglumf.dataservice.payload.response.sociallead.AdminSocialLeadListResponse;
import com.kadioglumf.dataservice.payload.response.sociallead.PartnerSocialLeadDetailResponse;
import com.kadioglumf.dataservice.payload.response.sociallead.PartnerSocialLeadListResponse;
import com.kadioglumf.dataservice.reader.util.DateUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

@Mapper(componentModel = "spring")
public interface SocialLeadMapper {

  @Mapping(
      source = "creationDateOfValue",
      target = "creationDateOfValue",
      qualifiedByName = "getCreationDateOfValue")
  SocialLeadModel toSocialLeadModel(SocialLeadImportRequest request);

  List<SocialLeadModel> toSocialLeadModelList(List<SocialLeadImportRequest> request);

  default Page<AdminSocialLeadListResponse> toAdminSocialLeadListResponse(
      Page<SocialLeadModel> models, @Context Set<UserDetailsResponse> userDetails) {
    return models.map(model -> toAdminSocialLeadListResponse(model, userDetails));
  }

  AdminSocialLeadListResponse toAdminSocialLeadListResponse(
      SocialLeadModel model, @Context Set<UserDetailsResponse> userDetails);

  default Page<PartnerSocialLeadListResponse> toPartnerSocialLeadListResponse(
      Page<SocialLeadModel> models) {
    return models.map(this::toPartnerSocialLeadListResponse);
  }

  PartnerSocialLeadListResponse toPartnerSocialLeadListResponse(SocialLeadModel model);

  PartnerSocialLeadDetailResponse toPartnerSocialLeadDetailResponse(
      SocialLeadModel model, @Context Set<UserDetailsResponse> userDetails);

  @Mapping(target = "author", source = "authorId", qualifiedByName = "getAuthor")
  CommentItemResponse toCommentsItem(
      SocialLeadCommentModel model, @Context Set<UserDetailsResponse> userDetails);

  @Mapping(target = "author", source = "authorId", qualifiedByName = "getAuthor")
  ActivityItemResponse toActivityItem(
      SocialLeadActivityModel model, @Context Set<UserDetailsResponse> userDetails);

  AdminSocialLeadDetailResponse toAdminSocialLeadDetailResponse(
      SocialLeadModel model, @Context Set<UserDetailsResponse> userDetails);

  @Named("getAuthor")
  default AuthorUserResponse getAuthor(
      Long authorId, @Context Set<UserDetailsResponse> userDetails) {
    if (CollectionUtils.isEmpty(userDetails)) {
      return null;
    }
    UserDetailsResponse author =
        userDetails.stream()
            .filter(user -> user.getUserId().equals(authorId))
            .findFirst()
            .orElse(null);
    return author == null ? null : new AuthorUserResponse(author.getName(), author.getSurname());
  }

  @Named("getCreationDateOfValue")
  default Date getCreationDateOfValue(String date) {
    ZoneId swissZoneId = ZoneId.of("Europe/Zurich");
    ZoneId gmtZoneId = ZoneId.of("UTC");
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern(DateUtils.determineDateFormatPattern(date));
    LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
    ZonedDateTime swissZonedDateTime = ZonedDateTime.of(localDateTime, swissZoneId);
    ZonedDateTime gmtZonedDateTime = swissZonedDateTime.withZoneSameInstant(gmtZoneId);

    return Date.from(gmtZonedDateTime.toInstant());
  }
}
