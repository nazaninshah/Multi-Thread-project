package com.project.shahbazi.spring_multiThread_pro.repository;

import com.project.shahbazi.spring_multiThread_pro.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<AccountEntity, Long> {
}
