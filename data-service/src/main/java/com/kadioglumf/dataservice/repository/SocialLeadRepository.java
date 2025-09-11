package com.kadioglumf.dataservice.repository;

import com.kadioglumf.dataservice.model.sociallead.SocialLeadModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SocialLeadRepository
    extends JpaRepository<SocialLeadModel, Long>, JpaSpecificationExecutor<SocialLeadModel> {

  Optional<SocialLeadModel> findByIdAndAssignedPartnerId(Long id, Long partnerId);
}
