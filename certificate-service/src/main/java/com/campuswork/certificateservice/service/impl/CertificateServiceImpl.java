package com.campuswork.certificateservice.service.impl;

import com.campuswork.certificateservice.client.AttendanceServiceClient;
import com.campuswork.certificateservice.client.JobServiceClient;
import com.campuswork.certificateservice.dto.AttendanceDto;
import com.campuswork.certificateservice.dto.CertificateResponse;
import com.campuswork.certificateservice.dto.IssueCertificateRequest;
import com.campuswork.certificateservice.dto.JobDto;
import com.campuswork.certificateservice.mapper.CertificateMapper;
import com.campuswork.certificateservice.model.Certificate;
import com.campuswork.certificateservice.repository.CertificateRepository;
import com.campuswork.certificateservice.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final CertificateMapper certificateMapper;
    private final JobServiceClient jobServiceClient;
    private final AttendanceServiceClient attendanceServiceClient;

    @Override
    public CertificateResponse issueCertificate(IssueCertificateRequest dto, Long issuerId, boolean isAdmin, String bearerToken) {
        JobDto job = jobServiceClient.getJob(dto.getJobId(), bearerToken);

        if (!isAdmin && !job.getPostedBy().equals(issuerId)) {
            throw new AccessDeniedException("You can only issue certificates for jobs you posted");
        }

        List<AttendanceDto> records = attendanceServiceClient.getStudentAttendance(dto.getStudentId(), bearerToken);

        long totalMinutes = records.stream()
                .filter(r -> r.getClockOut() != null)
                .mapToLong(r -> Duration.between(r.getClockIn(), r.getClockOut()).toMinutes())
                .sum();

        BigDecimal totalHours = BigDecimal.valueOf(totalMinutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        Certificate certificate = Certificate.builder()
                .studentId(dto.getStudentId())
                .jobId(dto.getJobId())
                .issuedBy(issuerId)
                .title(dto.getTitle())
                .totalHours(totalHours)
                .certificateNumber(generateCertificateNumber())
                .issueDate(LocalDate.now())
                .build();

        Certificate saved = certificateRepository.save(certificate);
        return certificateMapper.toResponse(saved);
    }

    @Override
    public List<CertificateResponse> getMyCertificates(Long studentId) {
        return certificateRepository.findByStudentId(studentId).stream()
                .map(certificateMapper::toResponse)
                .collect(Collectors.toList());
    }

    private String generateCertificateNumber() {
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}