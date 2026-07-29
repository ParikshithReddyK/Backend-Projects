package com.campuswork.certificateservice.service;

import com.campuswork.certificateservice.dto.CertificateResponse;
import com.campuswork.certificateservice.dto.IssueCertificateRequest;

import java.util.List;

public interface CertificateService {
    CertificateResponse issueCertificate(IssueCertificateRequest dto, Long issuerId, boolean isAdmin, String bearerToken);
    List<CertificateResponse> getMyCertificates(Long studentId);
}