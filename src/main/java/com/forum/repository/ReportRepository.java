package com.forum.repository;

import com.forum.model.Report;
import com.forum.model.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByStatus(ReportStatus unresolved);
    Optional<Report> findById(Long id);
}
