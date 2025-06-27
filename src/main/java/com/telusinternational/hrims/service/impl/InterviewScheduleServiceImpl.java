package com.telusinternational.hrims.service.impl;

import com.telusinternational.hrims.entity.InterviewSchedule;
import com.telusinternational.hrims.repository.InterviewScheduleRepository;
import com.telusinternational.hrims.service.GoogleCalendarService;
import com.telusinternational.hrims.service.InterviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InterviewScheduleServiceImpl implements InterviewScheduleService {

    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Override
    public InterviewSchedule scheduleInterview(InterviewSchedule schedule) {
        try {
            // Create Google Calendar event
            String emailId = schedule.getCandidateInfo().getEmail();
            List<String> interviewerEmails = schedule.getInterviewersInfo() != null ?
                schedule.getInterviewersInfo().stream()
                    .map(interviewer -> interviewer.getEmailId())
                    .toList() : List.of();
            List<String> emailList = new java.util.ArrayList<>();
            emailList.add(emailId);
            emailList.addAll(interviewerEmails);

            // Create event and get the Event object
            com.google.api.services.calendar.model.Event createdEvent = googleCalendarService.createInterviewEventAndReturnEvent(
                "Interview with " + schedule.getCandidateInfo().getName(),
                "Interview for " + schedule.getCandidateInfo().getPosition() + " position",
                LocalDateTime.of(schedule.getCalendarInfo().getDate(), schedule.getCalendarInfo().getStartTime()),
                LocalDateTime.of(schedule.getCalendarInfo().getDate(), schedule.getCalendarInfo().getEndTime()),
                emailList
            );

            // Save the schedule
            schedule.setCalendarLink(createdEvent.getHtmlLink());
            schedule.setCalendarEventId(createdEvent.getId());
            return interviewScheduleRepository.save(schedule);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Google Calendar event: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewSchedule> getAllInterviews() {
        return interviewScheduleRepository.findAll();
    }

    @Override
    public InterviewSchedule updateInterview(Long id, InterviewSchedule schedule) {
        return interviewScheduleRepository.findById(id)
                .map(existingSchedule -> {
                    schedule.setId(id);

                    // Preserve the calendarEventId
                    schedule.setCalendarEventId(existingSchedule.getCalendarEventId());

                    // Update Google Calendar event if eventId exists
                    if (existingSchedule.getCalendarEventId() != null) {
                        String emailId = schedule.getCandidateInfo().getEmail();
                        List<String> interviewerEmails = schedule.getInterviewersInfo() != null ?
                            schedule.getInterviewersInfo().stream()
                                .map(interviewer -> interviewer.getEmailId())
                                .toList() : List.of();
                        List<String> emailList = new java.util.ArrayList<>();
                        emailList.add(emailId);
                        emailList.addAll(interviewerEmails);

                        try {
                            com.google.api.services.calendar.model.Event updatedEvent =
                                googleCalendarService.updateEventAndReturn(
                                    existingSchedule.getCalendarEventId(),
                                    "Interview with " + schedule.getCandidateInfo().getName(),
                                    "Interview for " + schedule.getCandidateInfo().getPosition() + " position",
                                    LocalDateTime.of(schedule.getCalendarInfo().getDate(), schedule.getCalendarInfo().getStartTime()),
                                    LocalDateTime.of(schedule.getCalendarInfo().getDate(), schedule.getCalendarInfo().getEndTime()),
                                    emailList
                                );
                            // Update the calendarLink with the latest value
                            schedule.setCalendarLink(updatedEvent.getHtmlLink());
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to update Google Calendar event: " + e.getMessage(), e);
                        }
                    } else {
                        // If no eventId, preserve the existing link
                        schedule.setCalendarLink(existingSchedule.getCalendarLink());
                    }

                    return interviewScheduleRepository.save(schedule);
                })
                .orElseThrow(() -> new IllegalArgumentException("Interview not found with id: " + id));
    }

    @Override
    public void deleteInterview(Long id) {
        InterviewSchedule schedule = interviewScheduleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Interview not found with id: " + id));
        // Delete from Google Calendar
        if (schedule.getCalendarEventId() != null) {
            googleCalendarService.deleteEvent(schedule.getCalendarEventId());
        }
        interviewScheduleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewSchedule getInterviewById(Long id) {
        return interviewScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found with id: " + id));
    }
} 