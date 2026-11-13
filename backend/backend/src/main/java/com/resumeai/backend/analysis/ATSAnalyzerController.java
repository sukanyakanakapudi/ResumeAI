
package com.resumeai.backend.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.resumeai.backend.model.Resume;
import com.resumeai.backend.service.ResumeService;
import com.resumeai.backend.service.AnalysisHistoryService;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "http://localhost:3000")
public class ATSAnalyzerController {

    private final ATSAnalyzerService atsAnalyzerService;
    private final ResumeService resumeService;
    private final AnalysisHistoryService historyService;

    public ATSAnalyzerController(
            ATSAnalyzerService atsAnalyzerService,
            ResumeService resumeService,
            AnalysisHistoryService historyService) {

        this.atsAnalyzerService = atsAnalyzerService;
        this.resumeService = resumeService;
        this.historyService = historyService;
    }

    @PostMapping("/{id}")
    public Map<String, Object> analyzeResume(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        Resume resume = resumeService.getResumeById(id);

        if (resume == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Resume not found"
            );
        }

        String jobDescription = request.get("jobDescription");
        String resumeText = resume.getExtractedText();

        int score = atsAnalyzerService.calculateScore(
            resumeText, jobDescription);

        List<String> matchingSkills =
            atsAnalyzerService.getMatchingSkills(
                resumeText, jobDescription);

        List<String> missingSkills =
            atsAnalyzerService.getMissingSkills(
                resumeText, jobDescription);

        // Save analysis result in MySQL
        historyService.saveHistory(
            id,
            resume.getCandidateName(),
            score
        );

        Map<String, Object> result = new HashMap<>();

        result.put("resumeId", id);
        result.put("candidateName", resume.getCandidateName());
        result.put("atsScore", score);
        result.put("matchingSkills", matchingSkills);
        result.put("missingSkills", missingSkills);

        List<String> suggestions = new java.util.ArrayList<>();

        for (String skill : missingSkills) {
            suggestions.add(
                "Consider learning " + skill.toUpperCase()
                + " and adding relevant projects or experience to your resume."
            );
        }

        result.put("suggestions", suggestions);

        return result;
    }
}
