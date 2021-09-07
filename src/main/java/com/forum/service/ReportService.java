package com.forum.service;

import com.forum.dto.ReportDto;
import com.forum.model.Post;
import com.forum.model.Report;
import com.forum.model.User;
import com.forum.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    @Transactional
    public void saveReport(ReportDto reportDto, Post post, User user) {
        String comment = Objects.toString(reportDto.getReportComment(), "");
        String reason = Objects.toString(reportDto.getReportReason(), "");

        Report report = Report.builder()
                .reportComment(comment)
                .reportReason(reason)
                .post(post)
                .reporter(user)
                .build();

        reportRepository.save(report);
    }
}
