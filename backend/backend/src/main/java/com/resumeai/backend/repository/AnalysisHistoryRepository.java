package com.resumeai.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.resumeai.backend.model.AnalysisHistory;

public interface AnalysisHistoryRepository
        extends JpaRepository<AnalysisHistory, Long> {
}