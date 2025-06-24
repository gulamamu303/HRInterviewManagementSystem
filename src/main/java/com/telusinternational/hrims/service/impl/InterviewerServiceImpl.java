package com.telusinternational.hrims.service.impl;

import com.telusinternational.hrims.entity.Interviewer;
import com.telusinternational.hrims.repository.InterviewerRepository;
import com.telusinternational.hrims.service.InterviewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InterviewerServiceImpl implements InterviewerService {

    @Autowired
    private InterviewerRepository interviewerRepository;

    @Override
    public Interviewer addInterviewer(Interviewer interviewer) {
        if (interviewerRepository.existsByEmailId(interviewer.getEmailId())) {
            throw new IllegalArgumentException("Interviewer with email " + interviewer.getEmailId() + " already exists");
        }
        return interviewerRepository.save(interviewer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Interviewer> getAllInterviewers() {
        return interviewerRepository.findAll();
    }

    @Override
    public Interviewer updateInterviewer(Long id, Interviewer interviewer) {
        return interviewerRepository.findById(id)
                .map(existingInterviewer -> {
                    interviewer.setId(id);
                    return interviewerRepository.save(interviewer);
                })
                .orElseThrow(() -> new IllegalArgumentException("Interviewer not found with id: " + id));
    }

    @Override
    public void deleteInterviewer(Long id) {
        if (!interviewerRepository.existsById(id)) {
            throw new IllegalArgumentException("Interviewer not found with id: " + id);
        }
        interviewerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Interviewer getInterviewerById(Long id) {
        return interviewerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interviewer not found with id: " + id));
    }
} 