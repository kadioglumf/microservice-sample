package com.kadioglumf.authservice.repository;

import com.kadioglumf.authservice.models.UserLoginActivityModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginActivityRepository extends JpaRepository<UserLoginActivityModel, Long> {}
