package com.telusinternational.hrims.repository;

import com.telusinternational.hrims.entity.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    boolean existsByName(String name);
} 