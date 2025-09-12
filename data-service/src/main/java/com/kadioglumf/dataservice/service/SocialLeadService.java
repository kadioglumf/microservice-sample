package com.kadioglumf.dataservice.service;

import com.kadioglumf.dataservice.adapter.AuthServiceAdapter;
import com.kadioglumf.dataservice.constant.ExceptionConstants;
import com.kadioglumf.dataservice.core.SearchSpecification;
import com.kadioglumf.dataservice.core.exception.BusinessException;
import com.kadioglumf.dataservice.enums.*;
import com.kadioglumf.dataservice.mapper.SocialLeadMapper;
import com.kadioglumf.dataservice.model.sociallead.SocialLeadActivityModel;
import com.kadioglumf.dataservice.model.sociallead.SocialLeadCommentModel;
import com.kadioglumf.dataservice.model.sociallead.SocialLeadModel;
import com.kadioglumf.dataservice.payload.request.search.SearchRequest;
import com.kadioglumf.dataservice.payload.request.sociallead.*;
import com.kadioglumf.dataservice.payload.response.NewCommentResponse;
import com.kadioglumf.dataservice.payload.response.UserDetailsResponse;
import com.kadioglumf.dataservice.payload.response.sociallead.AdminSocialLeadDetailResponse;
import com.kadioglumf.dataservice.payload.response.sociallead.AdminSocialLeadListResponse;
import com.kadioglumf.dataservice.reader.service.ReaderService;
import com.kadioglumf.dataservice.repository.SocialLeadRepository;
import com.kadioglumf.dataservice.util.AuthUtils;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class SocialLeadService {

  private final SocialLeadRepository socialLeadRepository;
  private final ReaderService readerService;
  private final SocialLeadMapper socialLeadMapper;
  private final AuthServiceAdapter authServiceAdapter;

  public void create(SocialLeadImportRequest request) {
    var model = socialLeadMapper.toSocialLeadModel(request);
    model.setCreationDateOfValue(new Date());
    model.setStatus(SocialLeadStatus.OPEN);

    SocialLeadActivityModel activityModel =
        new SocialLeadActivityModel(null, null, ActivityType.CREATED, model);
    model.addActivity(activityModel);
    socialLeadRepository.save(model);
  }

  public void importFile(MultipartFile file) {
    try {
      List<SocialLeadImportRequest> socialLeads =
          readerService.readFile(
              SocialLeadImportRequest.class,
              file.getInputStream(),
              FilenameUtils.getExtension(file.getOriginalFilename()));
      if (CollectionUtils.isEmpty(socialLeads)) {
        throw new BusinessException(ExceptionConstants.FILE_READER_EMPTY);
      }
      var models = socialLeadMapper.toSocialLeadModelList(socialLeads);
      models.forEach(
          s -> {
            s.setStatus(SocialLeadStatus.OPEN);
            SocialLeadActivityModel activityModel =
                new SocialLeadActivityModel(null, null, ActivityType.CREATED, s);
            s.addActivity(activityModel);
          });

      socialLeadRepository.saveAll(models);
    } catch (Exception ex) {
      throw new BusinessException(ExceptionConstants.IMPORT_FILE_ERROR);
    }
  }

  public Page<AdminSocialLeadListResponse> listingForAdmin(SearchRequest request) {
    Page<SocialLeadModel> models = getSocialLeads(request, SearchRoleType.ADMIN);
    Set<Long> userIds =
        models.stream().map(SocialLeadModel::getAssignedPartnerId).collect(Collectors.toSet());
    userIds.remove(null);

    Set<UserDetailsResponse> userDetails = null;
    if (!CollectionUtils.isEmpty(userIds)) {
      userDetails = authServiceAdapter.getUserDetailsByUserIds(userIds);
    }

    return socialLeadMapper.toAdminSocialLeadListResponse(models, userDetails);
  }

  public AdminSocialLeadDetailResponse getSocialLeadDetailForAdmin(Long id) {
    var socialLeadModel = getById(id);
    Set<Long> userIds = new HashSet<>();
    userIds.add(socialLeadModel.getAssignedPartnerId());
    userIds.addAll(
        socialLeadModel.getActivities().stream()
            .filter(Objects::nonNull)
            .map(SocialLeadActivityModel::getAuthorId)
            .collect(Collectors.toSet()));
    userIds.addAll(
        socialLeadModel.getComments().stream()
            .filter(Objects::nonNull)
            .map(SocialLeadCommentModel::getAuthorId)
            .collect(Collectors.toSet()));
    userIds.remove(null);

    Set<UserDetailsResponse> userDetails = null;
    if (!CollectionUtils.isEmpty(userIds)) {
      userDetails = authServiceAdapter.getUserDetailsByUserIds(userIds);
    }
    return socialLeadMapper.toAdminSocialLeadDetailResponse(socialLeadModel, userDetails);
  }

  private Page<SocialLeadModel> getSocialLeads(SearchRequest request, SearchRoleType roleType) {
    SearchSpecification<SocialLeadModel> specification =
        new SearchSpecification<>(request, SearchExpressionType.GET_SOCIAL_LEADS, roleType);
    Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
    return socialLeadRepository.findAll(specification, pageable);
  }

  public void delete(Long id) {
    var socialLeadModel = getById(id);
    socialLeadRepository.delete(socialLeadModel);
  }

  private SocialLeadModel getById(Long id) {
    Optional<SocialLeadModel> socialLeadModel;
    socialLeadModel = socialLeadRepository.findById(id);
    if (socialLeadModel.isEmpty()) {
      throw new BusinessException(ExceptionConstants.SOCIAL_LEAD_NOT_FOUND);
    }
    return socialLeadModel.get();
  }

  public NewCommentResponse newComment(CreateSocialLeadCommentRequest request) {
    var socialLeadModel = getById(request.getId());

    SocialLeadCommentModel commentModel =
        new SocialLeadCommentModel(request.getMessage(), socialLeadModel);
    SocialLeadActivityModel activityModel =
        new SocialLeadActivityModel(
            commentModel.getCommentId(), null, ActivityType.NEW_COMMENT, socialLeadModel);

    socialLeadModel.addActivity(activityModel);
    socialLeadModel.addComment(commentModel);
    socialLeadRepository.save(socialLeadModel);
    return new NewCommentResponse(commentModel.getCommentId());
  }
}
