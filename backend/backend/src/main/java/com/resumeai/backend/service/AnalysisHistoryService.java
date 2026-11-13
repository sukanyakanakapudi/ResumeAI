package com.resumeai.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.resumeai.backend.model.AnalysisHistory;
import com.resumeai.backend.repository.AnalysisHistoryRepository;

@Service
public class AnalysisHistoryService {

    private final AnalysisHistoryRepository repository;

    public AnalysisHistoryService(
            AnalysisHistoryRepository repository) {
        this.repository = repository;
    }

    public AnalysisHistory saveHistory(
            Long resumeId,
            String candidateName,
            int atsScore) {

        AnalysisHistory history = new AnalysisHistory();

        history.setResumeId(resumeId);
        history.setCandidateName(candidateName);
        history.setAtsScore(atsScore);
        history.setAnalysisDate(LocalDateTime.now());

        return repository.save(history);
    }

    public List<AnalysisHistory> getAllHistory() {
        return repository.findAll();
    }
}