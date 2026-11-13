
package com.resumeai.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.resumeai.backend.model.Resume;
import com.resumeai.backend.service.ResumeService;
import org.springframework.http.MediaType;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.List; 

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "http://localhost:3000")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // 1. Create Resume
    @PostMapping
    public Resume createResume(@RequestBody Resume resume) {
        return resumeService.saveResume(resume);
    }

@PostMapping(
    value = "/upload",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
public Resume uploadResume(
        @RequestParam("file") MultipartFile file,
        @RequestParam("candidateName") String candidateName)
        throws IOException {

    String extractedText;

    try (PDDocument document = Loader.loadPDF(file.getBytes())) {
        extractedText = new PDFTextStripper().getText(document);
    }

    Resume resume = new Resume();
    resume.setFileName(file.getOriginalFilename());
    resume.setCandidateName(candidateName);
    resume.setExtractedText(extractedText);

    return resumeService.saveResume(resume);
}

    // 2. Get All Resumes
    @GetMapping
    public List<Resume> getAllResumes() {
        return resumeService.getAllResumes();
    }

    // 3. Get Resume By ID
    @GetMapping("/{id}")
    public Resume getResumeById(@PathVariable Long id) {

        Resume resume = resumeService.getResumeById(id);

        if (resume == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                "Resume not found"
            );
        }

        return resume;
    }

    // 4. Update Resume
    @PutMapping("/{id}")
    public Resume updateResume(
            @PathVariable Long id,
            @RequestBody Resume updatedResume) {

        Resume existingResume = resumeService.getResumeById(id);

        if (existingResume == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                "Resume not found"
            );
        }

        existingResume.setFileName(updatedResume.getFileName());
        existingResume.setCandidateName(updatedResume.getCandidateName());
        existingResume.setExtractedText(updatedResume.getExtractedText());

        return resumeService.saveResume(existingResume);
    }

    // 5. Delete Resume
    @DeleteMapping("/{id}")
    public String deleteResume(@PathVariable Long id) {

        Resume resume = resumeService.getResumeById(id);

        if (resume == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                "Resume not found"
            );
        }

        resumeService.deleteResume(id);

        return "Resume deleted successfully";
    }
}
