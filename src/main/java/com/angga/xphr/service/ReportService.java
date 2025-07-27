package com.angga.xphr.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.angga.xphr.repository.TimeRecordRepository;
import com.angga.xphr.model.dto.ReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private TimeRecordRepository timeRecordRepository;

    @Cacheable(value = "reportCache", key = "'getReport_' + #startDate + '_' + #endDate")
    public List<ReportDTO> getReport(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Generating report for ALL users - START: {}, END: {}", startDate, endDate);
        List<Object[]> results = timeRecordRepository.findReportNative(startDate, endDate);
        logger.info("Fetched {} records", results.size());

        List<ReportDTO> reportList = new ArrayList<>();
        for (Object[] row : results) {
            String employeeName = row[0] != null ? (String) row[0] : "-";
            String projectName = row[1] != null ? (String) row[1] : "-";
            double totalHours = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            reportList.add(new ReportDTO(employeeName, projectName, totalHours));
        }

        return reportList;
    }

    @Cacheable(value = "reportCache", key = "'getReportForEmployee_' + #username + '_' + #start + '_' + #end")
    public List<ReportDTO> getReportForEmployee(String username, LocalDateTime start, LocalDateTime end) {
        logger.info("Generating report for USER: {} - START: {}, END: {}", username, start, end);
        List<Object[]> results = timeRecordRepository.findReportsByEmployeeUsername(username, start, end);
        logger.info("Fetched {} records for {}", results.size(), username);

        List<ReportDTO> reportList = new ArrayList<>();
        for (Object[] row : results) {
            String employeeName = row[0] != null ? (String) row[0] : "-";
            String projectName = row[1] != null ? (String) row[1] : "-";
            double totalHours = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            reportList.add(new ReportDTO(employeeName, projectName, totalHours));
        }

        return reportList;
    }

}
