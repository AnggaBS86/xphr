package com.angga.xphr.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.angga.xphr.repository.TimeRecordRepository;
import com.angga.xphr.model.dto.ReportDTO;

@Service
public class ReportService {

    @Autowired
    private TimeRecordRepository timeRecordRepository;

    public List<ReportDTO> getReport(LocalDateTime startDate, LocalDateTime endDate) {
        System.out.println("START: " + startDate + ", END: " + endDate);
        List<Object[]> results = timeRecordRepository.findReportNative(startDate, endDate);
        System.out.println("RESULTS SIZE: " + results.size());
        List<ReportDTO> reportList = new ArrayList<>();
        for (Object[] row : results) {
            String employeeName = (String) row[0];
            String projectName = (String) row[1];
            double totalHours = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            reportList.add(new ReportDTO(employeeName, projectName, totalHours));
        }

        return reportList;
    }

    public List<ReportDTO> getReportForEmployee(String username, LocalDateTime start, LocalDateTime end) {
        System.out.println("START: " + start + ", END: " + end);
        var results = timeRecordRepository.findReportsByEmployeeUsername(username, start, end);
        System.out.println("RESULTS SIZE: " + results.size());

        List<ReportDTO> reportList = new ArrayList<>();
        for (Object[] row : results) {
            String employeeName = (String) row[0];
            String projectName = (String) row[1];
            double totalHours = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            reportList.add(new ReportDTO(employeeName, projectName, totalHours));
        }

        return reportList;
    }

}
