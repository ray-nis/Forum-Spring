package com.forum.controller.v1;

import com.forum.exception.ResourceNotFoundException;
import com.forum.model.Report;
import com.forum.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/moderator")
@RequiredArgsConstructor
public class ModeratorController {
    private final ReportService reportService;

    @GetMapping("")
    public String moderatorPage() {
        return "moderator/menu";
    }

    @GetMapping("/reports")
    public String getReports(Model model) {
        model.addAttribute("reports", reportService.getUnresolvedReports());
        return "moderator/reports";
    }

    @PostMapping("/reports/{id}")
    public String resolveReport(@PathVariable("id") Long id, Model model) throws ResourceNotFoundException {
        Report report = reportService.getReport(id);
        reportService.resolveReport(report);
        return "redirect:/moderator/reports";
    }
}
