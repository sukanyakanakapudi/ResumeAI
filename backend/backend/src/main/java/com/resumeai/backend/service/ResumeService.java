
package com.resumeai.backend.service;

import org.springframework.stereotype.Service;
import com.resumeai.backend.model.Resume;
import com.resumeai.backend.repository.ResumeRepository;

import java.util.List;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Resume saveResume(Resume resume) {
        return resumeRepository.save(resume);
    }

    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }

    public Resume getResumeById(Long id) {
        return resumeRepository.findById(id).orElse(null);
    }

    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }
}

