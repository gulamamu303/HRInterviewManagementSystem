package com.telusinternational.hrims.controller;

import com.telusinternational.hrims.entity.InterviewSchedule;
import com.telusinternational.hrims.service.InterviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class InterviewScheduleController {

    @Autowired
    private InterviewScheduleService interviewScheduleService;

    @GetMapping
    public ResponseEntity<List<InterviewSchedule>> getAllInterviews() {
        return ResponseEntity.ok(interviewScheduleService.getAllInterviews());
    }

    @PostMapping
    public ResponseEntity<InterviewSchedule> scheduleInterview(@RequestBody InterviewSchedule schedule) {
        System.out.println("Received schedule request: " + schedule);
        try {
            return ResponseEntity.ok(interviewScheduleService.scheduleInterview(schedule));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewSchedule> updateInterview(@PathVariable Long id, @RequestBody InterviewSchedule schedule) {
        try {
            return ResponseEntity.ok(interviewScheduleService.updateInterview(id, schedule));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        try {
            interviewScheduleService.deleteInterview(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 