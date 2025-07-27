package com.angga.xphr.controller;

import com.angga.xphr.service.ReportService;
import com.angga.xphr.model.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/report")
    public String getReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Model model,
            Authentication authentication) {

        var username = authentication.getName();
        var isAdmin = isAdmin(authentication);

        List<ReportDTO> reportData;
        if (isAdmin) {
            reportData = reportService.getReport(start, end);
        } else {
            reportData = reportService.getReportForEmployee(username, start, end);
        }

        model.addAttribute("report", reportData);
        return "report";
    }

    private boolean isAdmin(Authentication authentication) {
        for (var authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        
        return false;
    }
}
