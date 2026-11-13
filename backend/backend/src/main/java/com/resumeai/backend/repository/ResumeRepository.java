
package com.resumeai.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.resumeai.backend.model.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

}


