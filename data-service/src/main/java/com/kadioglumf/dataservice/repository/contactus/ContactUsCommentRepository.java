package com.kadioglumf.dataservice.repository.contactus;

import com.kadioglumf.dataservice.model.contactus.ContactUsCommentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactUsCommentRepository extends JpaRepository<ContactUsCommentModel, Long> {}
