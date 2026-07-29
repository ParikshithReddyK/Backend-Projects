package com.campuswork.certificateservice.repository;

import com.campuswork.certificateservice.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByStudentId(Long studentId);
    List<Certificate> findByJobId(Long jobId);
}