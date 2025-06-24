package com.telusinternational.hrims.service;

import com.telusinternational.hrims.entity.Candidate;
import java.util.List;

public interface CandidateService {
    Candidate addCandidate(Candidate candidate);
    List<Candidate> getAllCandidates();
    Candidate updateCandidate(Long id, Candidate candidate);
    void deleteCandidate(Long id);
    Candidate getCandidateById(Long id);
} 