package com.kadioglumf.dataservice.repository.contactus;

import com.kadioglumf.dataservice.model.contactus.ContactUsModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContactUsRepository
    extends JpaRepository<ContactUsModel, Long>, JpaSpecificationExecutor<ContactUsModel> {

  Optional<ContactUsModel> findByContactRequestId(String contactRequestId);

  Optional<ContactUsModel> findByContactRequestIdAndAssignedUserId(
      String contactRequestId, Long assignedUserId);
}
