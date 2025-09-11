package com.kadioglumf.dataservice.service;

import com.kadioglumf.dataservice.adapter.AuthServiceAdapter;
import com.kadioglumf.dataservice.constant.ExceptionConstants;
import com.kadioglumf.dataservice.core.SearchSpecification;
import com.kadioglumf.dataservice.core.exception.BusinessException;
import com.kadioglumf.dataservice.enums.*;
import com.kadioglumf.dataservice.mapper.ContactUsMapper;
import com.kadioglumf.dataservice.model.contactus.ContactUsActivityModel;
import com.kadioglumf.dataservice.model.contactus.ContactUsCommentModel;
import com.kadioglumf.dataservice.model.contactus.ContactUsModel;
import com.kadioglumf.dataservice.payload.request.contactus.CreateContactUsRequest;
import com.kadioglumf.dataservice.payload.request.search.SearchRequest;
import com.kadioglumf.dataservice.payload.response.NewCommentResponse;
import com.kadioglumf.dataservice.payload.response.UserDetailsResponse;
import com.kadioglumf.dataservice.payload.response.contactus.*;
import com.kadioglumf.dataservice.repository.contactus.ContactUsActivityRepository;
import com.kadioglumf.dataservice.repository.contactus.ContactUsCommentRepository;
import com.kadioglumf.dataservice.repository.contactus.ContactUsRepository;
import com.kadioglumf.dataservice.util.AppUtils;
import com.kadioglumf.dataservice.util.AuthUtils;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactUsService {

  private final ContactUsRepository contactUsRepository;
  private final ContactUsMapper contactUsMapper;
  private final AuthServiceAdapter authServiceAdapter;
  private final ContactUsActivityRepository activityRepository;
  private final ContactUsCommentRepository commentRepository;

  public CreateContactUsResponse createContactUs(CreateContactUsRequest request) {
    ContactUsModel model = contactUsMapper.toContactRequestModel(request);
    model.setContactRequestId(AppUtils.generateId("CT"));
    model.setUserId(AuthUtils.getUserId());
    model.setIsRegistered(AuthUtils.getUserId() != null);
    model = contactUsRepository.save(model);

    ContactUsActivityModel activityModel =
        new ContactUsActivityModel(null, null, ActivityType.CREATED, model);

    activityRepository.save(activityModel);

    // sendCreateContactUsEmail(model, null);

    // TODO sendNotificationToWebSocketClients(model.getContactRequestId(),
    // WsCategoryType.CONTACT_REQUEST_CREATED, WsInfoType.NEW_REQUEST, WsSendingType.ROLE_BASED,
    // RoleTypeEnum.ROLE_ADMIN, null);
    return new CreateContactUsResponse(model.getContactRequestId());
  }

  public Page<GetAdminContactUsResponse> getContactRequestsForAdmin(
      SearchRequest request, SearchRoleType roleType) {
    Page<ContactUsModel> models = getContactUsModelsByFilters(request, roleType);
    Set<Long> userIds =
        models.stream().map(ContactUsModel::getAssignedUserId).collect(Collectors.toSet());
    userIds.remove(null);

    Set<UserDetailsResponse> userDetails = null;
    if (!CollectionUtils.isEmpty(userIds)) {
      userDetails = authServiceAdapter.getUserDetailsByUserIds(userIds);
    }

    return contactUsMapper.toGetAdminContactUsResponse(models, userDetails);
  }

  public ContactUsDetailResponse getContactRequestDetail(
      String contactRequestId, SearchRoleType roleType) {
    ContactUsModel model = getByContactRequestIdAndAssignedUserId(contactRequestId, roleType);

    Set<Long> userIds = new HashSet<>();
    userIds.addAll(
        model.getActivities().stream()
            .filter(Objects::nonNull)
            .map(ContactUsActivityModel::getAuthorId)
            .collect(Collectors.toSet()));
    userIds.addAll(
        model.getComments().stream()
            .filter(Objects::nonNull)
            .map(ContactUsCommentModel::getAuthorId)
            .collect(Collectors.toSet()));
    userIds.add(model.getAssignedUserId());
    userIds.remove(null);

    Set<UserDetailsResponse> userDetails = null;
    if (!CollectionUtils.isEmpty(userIds)) {
      userDetails = authServiceAdapter.getUserDetailsByUserIds(userIds);
    }

    return contactUsMapper.toContactUsDetailResponse(model, userDetails);
  }

  public NewCommentResponse newComment(
      CreateContactUsCommentRequest request, SearchRoleType roleType) {
    ContactUsModel contactUsModel =
        getByContactRequestIdAndAssignedUserId(request.getContactRequestId(), roleType);

    ContactUsCommentModel commentModel =
        new ContactUsCommentModel(request.getMessage(), contactUsModel);
    commentRepository.save(commentModel);

    ContactUsActivityModel activityModel =
        new ContactUsActivityModel(
            commentModel.getCommentId(), null, ActivityType.NEW_COMMENT, contactUsModel);
    activityRepository.save(activityModel);

    return new NewCommentResponse(commentModel.getCommentId());
  }

  private ContactUsModel getByContactRequestIdAndAssignedUserId(
      String contactRequestId, SearchRoleType roleType) {
    Optional<ContactUsModel> contactUsModel;
    if (SearchRoleType.ADMIN.equals(roleType)) {
      contactUsModel = contactUsRepository.findByContactRequestId(contactRequestId);
    } else {
      contactUsModel =
          contactUsRepository.findByContactRequestIdAndAssignedUserId(
              contactRequestId, AuthUtils.getUserId());
    }
    if (contactUsModel.isEmpty()) {
      throw new BusinessException(ExceptionConstants.CONTACT_US_NOT_FOUND);
    }
    return contactUsModel.get();
  }

  private Page<ContactUsModel> getContactUsModelsByFilters(
      SearchRequest request, SearchRoleType roleType) {
    SearchSpecification<ContactUsModel> specification =
        new SearchSpecification<>(request, SearchExpressionType.GET_CONTACT_US, roleType);
    Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
    return contactUsRepository.findAll(specification, pageable);
  }

  public void deleteContactUsRequest(String contactRequestId) {
    var contactUs = contactUsRepository.findByContactRequestId(contactRequestId);
    if (contactUs.isEmpty()) {
      throw new BusinessException(ExceptionConstants.CONTACT_US_NOT_FOUND);
    }
    var model = contactUs.get();
    model.setIsDeleted(true);
    contactUsRepository.save(model);

    ContactUsActivityModel activityModel =
        new ContactUsActivityModel(
            Boolean.FALSE.toString(), Boolean.TRUE.toString(), ActivityType.DELETED, model);
    activityRepository.save(activityModel);
  }
}
