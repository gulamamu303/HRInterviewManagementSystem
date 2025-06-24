package com.telusinternational.hrims.service.impl;

import com.telusinternational.hrims.entity.Technology;
import com.telusinternational.hrims.repository.TechnologyRepository;
import com.telusinternational.hrims.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TechnologyServiceImpl implements TechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;

    @Override
    public Technology addTechnology(Technology technology) {
        if (technologyRepository.existsByName(technology.getName())) {
            throw new IllegalArgumentException("Technology with name " + technology.getName() + " already exists");
        }
        return technologyRepository.save(technology);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Technology> getAllTechnologies() {
        return technologyRepository.findAll();
    }

    @Override
    public Technology updateTechnology(Long id, Technology technology) {
        return technologyRepository.findById(id)
                .map(existingTech -> {
                    technology.setId(id);
                    return technologyRepository.save(technology);
                })
                .orElseThrow(() -> new IllegalArgumentException("Technology not found with id: " + id));
    }

    @Override
    public void deleteTechnology(Long id) {
        if (!technologyRepository.existsById(id)) {
            throw new IllegalArgumentException("Technology not found with id: " + id);
        }
        technologyRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Technology getTechnologyById(Long id) {
        return technologyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Technology not found with id: " + id));
    }
} 