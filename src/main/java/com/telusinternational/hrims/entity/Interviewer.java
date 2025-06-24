package com.telusinternational.hrims.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interviewers")
public class Interviewer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String emailId;
    
    @Column(nullable = false)
    private String designation;
    
    @Column(nullable = false)
    private String department;
    
    @ManyToMany
    @JoinTable(
        name = "interviewer_technologies",
        joinColumns = @JoinColumn(name = "interviewer_id"),
        inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private Set<Technology> tech;
} 