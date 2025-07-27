package com.angga.xphr.controller;

import com.angga.xphr.service.ReportService;
import com.angga.xphr.model.dto.ReportDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ReportController {

    private final ReportService reportService;
    private final String reportPage = "report";

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report")
    public String getReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Model model,
            Authentication authentication) {

        var username = authentication.getName();
        var isAdmin = hasRole(authentication, "ROLE_ADMIN");

        List<ReportDTO> reportData = isAdmin
                ? reportService.getReport(start, end)
                : reportService.getReportForEmployee(username, start, end);

        model.addAttribute(this.reportPage, reportData);
        return this.reportPage;
    }

    private boolean hasRole(Authentication authentication, String role) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return roles.contains(role);
    }
}
