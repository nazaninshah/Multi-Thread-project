package com.project.shahbazi.spring_multiThread_pro.repository;

import com.project.shahbazi.spring_multiThread_pro.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<CustomerEntity, Long> {
    CustomerEntity findByCustomerId(Long customerId);
}
