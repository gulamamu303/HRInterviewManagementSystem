package com.telusinternational.hrims.service.impl;

import com.telusinternational.hrims.entity.Candidate;
import com.telusinternational.hrims.repository.CandidateRepository;
import com.telusinternational.hrims.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public Candidate addCandidate(Candidate candidate) {
        if (candidateRepository.existsByEmail(candidate.getEmail())) {
            throw new IllegalArgumentException("Candidate with email " + candidate.getEmail() + " already exists");
        }
        return candidateRepository.save(candidate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate updateCandidate(Long id, Candidate candidate) {
        return candidateRepository.findById(id)
                .map(existingCandidate -> {
                    candidate.setId(id);
                    return candidateRepository.save(candidate);
                })
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found with id: " + id));
    }

    @Override
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new IllegalArgumentException("Candidate not found with id: " + id);
        }
        candidateRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found with id: " + id));
    }
} 