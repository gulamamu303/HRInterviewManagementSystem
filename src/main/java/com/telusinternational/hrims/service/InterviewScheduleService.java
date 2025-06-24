package com.telusinternational.hrims.service;

import com.telusinternational.hrims.entity.InterviewSchedule;
import java.util.List;

public interface InterviewScheduleService {
    InterviewSchedule scheduleInterview(InterviewSchedule schedule);
    List<InterviewSchedule> getAllInterviews();
    InterviewSchedule updateInterview(Long id, InterviewSchedule schedule);
    void deleteInterview(Long id);
    InterviewSchedule getInterviewById(Long id);
} 