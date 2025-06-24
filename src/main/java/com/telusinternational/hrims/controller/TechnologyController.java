package com.telusinternational.hrims.controller;

import com.telusinternational.hrims.entity.Technology;
import com.telusinternational.hrims.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tech")
public class TechnologyController {

    @Autowired
    private TechnologyService technologyService;

    @PostMapping
    public ResponseEntity<Technology> addTech(@RequestBody Technology technology) {
        try {
            return ResponseEntity.ok(technologyService.addTechnology(technology));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Technology>> getAllTech() {
        return ResponseEntity.ok(technologyService.getAllTechnologies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Technology> updateTech(@PathVariable Long id, @RequestBody Technology technology) {
        try {
            return ResponseEntity.ok(technologyService.updateTechnology(id, technology));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTech(@PathVariable Long id) {
        try {
            technologyService.deleteTechnology(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 