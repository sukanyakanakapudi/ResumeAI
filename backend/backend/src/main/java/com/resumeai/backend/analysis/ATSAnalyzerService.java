
package com.resumeai.backend.analysis;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ATSAnalyzerService {

    public int calculateScore(String resumeText, String jobDescription) {

        if (resumeText == null || jobDescription == null
                || resumeText.isBlank() || jobDescription.isBlank()) {
            return 0;
        }

        String resume = resumeText.toLowerCase();
        String job = jobDescription.toLowerCase();

        String[] skills = {
            "java",
            "spring boot",
            "react",
            "mysql",
            "sql",
            "javascript",
            "html",
            "css",
            "rest api",
            "aws",
            "docker",
            "python"
        };

        int totalSkills = 0;
        int matchedSkills = 0;

        for (String skill : skills) {
            if (job.contains(skill)) {
                totalSkills++;

                if (resume.contains(skill)) {
                    matchedSkills++;
                }
            }
        }

        if (totalSkills == 0) {
            return 0;
        }

        return (matchedSkills * 100) / totalSkills;
    }

    public List<String> getMatchingSkills(
            String resumeText, String jobDescription) {

        List<String> matchingSkills = new ArrayList<>();

        if (resumeText == null || jobDescription == null) {
            return matchingSkills;
        }

        String resume = resumeText.toLowerCase();
        String job = jobDescription.toLowerCase();

        String[] skills = {
            "java", "spring boot", "react", "mysql",
            "sql", "javascript", "html", "css",
            "rest api", "aws", "docker", "python"
        };

        for (String skill : skills) {
            if (job.contains(skill) && resume.contains(skill)) {
                matchingSkills.add(skill);
            }
        }

        return matchingSkills;
    }

    public List<String> getMissingSkills(
            String resumeText, String jobDescription) {

        List<String> missingSkills = new ArrayList<>();

        if (resumeText == null || jobDescription == null) {
            return missingSkills;
        }

        String resume = resumeText.toLowerCase();
        String job = jobDescription.toLowerCase();

        String[] skills = {
            "java", "spring boot", "react", "mysql",
            "sql", "javascript", "html", "css",
            "rest api", "aws", "docker", "python"
        };

        for (String skill : skills) {
            if (job.contains(skill) && !resume.contains(skill)) {
                missingSkills.add(skill);
            }
        }

        return missingSkills;
    }
}
