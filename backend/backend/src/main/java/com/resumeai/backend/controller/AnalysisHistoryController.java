package com.resumeai.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.resumeai.backend.model.AnalysisHistory;
import com.resumeai.backend.service.AnalysisHistoryService;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalysisHistoryController {

    private final AnalysisHistoryService historyService;

    public AnalysisHistoryController(
            AnalysisHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<AnalysisHistory> getHistory() {
        return historyService.getAllHistory();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisHistory> getHistoryById(
            @PathVariable Long id) {
        return ResponseEntity.notFound().build();
    }
}