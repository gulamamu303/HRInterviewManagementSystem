package com.telusinternational.hrims.controller;

import com.telusinternational.hrims.entity.Interviewer;
import com.telusinternational.hrims.service.InterviewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interviewer")
public class InterviewerController {

    @Autowired
    private InterviewerService interviewerService;

    @GetMapping
    public ResponseEntity<List<Interviewer>> getAllInterviewers() {
        return ResponseEntity.ok(interviewerService.getAllInterviewers());
    }

    @PostMapping
    public ResponseEntity<Interviewer> addInterviewer(@RequestBody Interviewer interviewer) {
        try {
            return ResponseEntity.ok(interviewerService.addInterviewer(interviewer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interviewer> updateInterviewer(@PathVariable Long id, @RequestBody Interviewer interviewer) {
        try {
            return ResponseEntity.ok(interviewerService.updateInterviewer(id, interviewer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterviewer(@PathVariable Long id) {
        try {
            interviewerService.deleteInterviewer(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 