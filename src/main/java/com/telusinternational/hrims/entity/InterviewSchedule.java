package com.telusinternational.hrims.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interview_schedules")
public class InterviewSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidateInfo;
    
    @ManyToMany
    @JoinTable(
        name = "schedule_interviewers",
        joinColumns = @JoinColumn(name = "schedule_id"),
        inverseJoinColumns = @JoinColumn(name = "interviewer_id")
    )
    private Set<Interviewer> interviewersInfo;
    
    @Embedded
    private CalendarInfo calendarInfo;
    
    private String calendarLink;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalendarInfo {
        @Column(nullable = false)
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate date;
        
        @Column(nullable = false)
        @JsonFormat(pattern = "HH:mm:ss")
        private LocalTime startTime;
        
        @Column(nullable = false)
        @JsonFormat(pattern = "HH:mm:ss")
        private LocalTime endTime;
    }
} 