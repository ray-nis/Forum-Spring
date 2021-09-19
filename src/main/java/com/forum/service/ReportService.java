package com.forum.service;

import com.forum.dto.ReportDto;
import com.forum.exception.ResourceNotFoundException;
import com.forum.model.Post;
import com.forum.model.Report;
import com.forum.model.User;
import com.forum.model.enums.ReportStatus;
import com.forum.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    @Transactional
    public void saveReport(ReportDto reportDto, Post post, User user) {
        String comment = Objects.toString(reportDto.getReportComment(), "");
        String reason = Objects.toString(reportDto.getReportReason(), "other");

        Report report = Report.builder()
                .reportComment(comment)
                .reportReason(reason)
                .post(post)
                .reporter(user)
                .status(ReportStatus.UNRESOLVED)
                .build();

        reportRepository.save(report);
    }

    public List<Report> getUnresolvedReports() {
        return reportRepository.findAllByStatus(ReportStatus.UNRESOLVED);
    }

    public Report getReport(Long id) throws ResourceNotFoundException {
        return reportRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public void resolveReport(Report report) {
        report.setStatus(ReportStatus.RESOLVED);
    }
}
