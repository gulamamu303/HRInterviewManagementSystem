package com.telusinternational.hrims.repository;

import com.telusinternational.hrims.entity.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {
    List<InterviewSchedule> findByCalendarInfo_Date(LocalDate date);
    List<InterviewSchedule> findByCalendarInfo_DateBetween(LocalDate startDate, LocalDate endDate);
} 