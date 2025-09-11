package com.kadioglumf.dataservice.mapper;

import com.kadioglumf.dataservice.model.contactus.ContactUsActivityModel;
import com.kadioglumf.dataservice.model.contactus.ContactUsCommentModel;
import com.kadioglumf.dataservice.model.contactus.ContactUsModel;
import com.kadioglumf.dataservice.payload.request.contactus.CreateContactUsRequest;
import com.kadioglumf.dataservice.payload.response.*;
import com.kadioglumf.dataservice.payload.response.contactus.ContactUsDetailResponse;
import com.kadioglumf.dataservice.payload.response.contactus.GetAdminContactUsResponse;
import com.kadioglumf.dataservice.payload.response.contactus.GetContactUsResponse;
import java.util.Set;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

@Mapper(componentModel = "spring")
public interface ContactUsMapper {

  ContactUsModel toContactRequestModel(CreateContactUsRequest request);

  default Page<GetContactUsResponse> toGetContactUsResponse(Page<ContactUsModel> model) {
    return model.map(this::toGetContactUsResponse);
  }

  GetContactUsResponse toGetContactUsResponse(ContactUsModel model);

  default Page<GetAdminContactUsResponse> toGetAdminContactUsResponse(
      Page<ContactUsModel> models, @Context Set<UserDetailsResponse> userDetails) {
    return models.map(model -> toGetAdminContactUsResponse(model, userDetails));
  }

  GetAdminContactUsResponse toGetAdminContactUsResponse(
      ContactUsModel model, @Context Set<UserDetailsResponse> userDetails);

  ContactUsDetailResponse toContactUsDetailResponse(
      ContactUsModel model, @Context Set<UserDetailsResponse> userDetails);

  @Mapping(target = "author", source = "authorId", qualifiedByName = "getAuthor")
  CommentItemResponse toCommentsItem(
      ContactUsCommentModel model, @Context Set<UserDetailsResponse> userDetails);

  @Mapping(target = "author", source = "authorId", qualifiedByName = "getAuthor")
  ActivityItemResponse toActivityItem(
      ContactUsActivityModel model, @Context Set<UserDetailsResponse> userDetails);

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
}
