package com.kadioglumf.dataservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kadioglumf.dataservice.base.BaseTest;
import com.kadioglumf.dataservice.repository.contactus.ContactUsRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContactUsServiceTest extends BaseTest {

  @InjectMocks private ContactUsService service;
  @Mock private ContactUsRepository consultingRequestRepository;
}
