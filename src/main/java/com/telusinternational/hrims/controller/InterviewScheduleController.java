package com.telusinternational.hrims.controller;

import com.telusinternational.hrims.entity.InterviewSchedule;
import com.telusinternational.hrims.service.InterviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class InterviewScheduleController {

    @Autowired
    private InterviewScheduleService interviewScheduleService;

    @GetMapping
    public ResponseEntity<?> getAllInterviews(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            Map<String, Object> errorResponse = Map.of(
                "code", 400, // Corrected to 400 as per typical error handling for invalid input
                "message", "Invalid input- End Date should be greater than start date"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(interviewScheduleService.getAllInterviews(startDate, endDate));
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