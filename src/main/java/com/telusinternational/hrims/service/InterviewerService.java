package com.telusinternational.hrims.service;

import com.telusinternational.hrims.entity.Interviewer;
import java.util.List;

public interface InterviewerService {
    Interviewer addInterviewer(Interviewer interviewer);
    List<Interviewer> getAllInterviewers();
    Interviewer updateInterviewer(Long id, Interviewer interviewer);
    void deleteInterviewer(Long id);
    Interviewer getInterviewerById(Long id);
} 