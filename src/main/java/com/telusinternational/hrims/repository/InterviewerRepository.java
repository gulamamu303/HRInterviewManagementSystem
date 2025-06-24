package com.telusinternational.hrims.repository;

import com.telusinternational.hrims.entity.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
    boolean existsByEmailId(String emailId);
} 