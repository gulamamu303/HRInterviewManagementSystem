package com.telusinternational.hrims.service;

import com.telusinternational.hrims.entity.Technology;
import java.util.List;

public interface TechnologyService {
    Technology addTechnology(Technology technology);
    List<Technology> getAllTechnologies();
    Technology updateTechnology(Long id, Technology technology);
    void deleteTechnology(Long id);
    Technology getTechnologyById(Long id);
} 