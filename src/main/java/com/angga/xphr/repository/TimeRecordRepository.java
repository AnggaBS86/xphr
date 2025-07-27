package com.angga.xphr.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.angga.xphr.model.TimeRecord;

public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {

    @Query(value = """
            SELECT e.name AS employee_name,
                   p.name AS project_name,
                   SUM(EXTRACT(EPOCH FROM (tr.time_to - tr.time_from)) / 3600) AS total_hours
            FROM time_record tr
            JOIN employee e ON tr.employee_id = e.id
            JOIN project p ON tr.project_id = p.id
            WHERE tr.time_from BETWEEN :startDate AND :endDate
            GROUP BY e.name, p.name
            ORDER BY e.name, p.name
            """, nativeQuery = true)
    List<Object[]> findReportNative(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = """
            SELECT e.name AS employee_name,
                   p.name AS project_name,
                   SUM(EXTRACT(EPOCH FROM (tr.time_to - tr.time_from)) / 3600) AS total_hours
            FROM time_record tr
            JOIN employee e ON tr.employee_id = e.id
            JOIN project p ON tr.project_id = p.id
            WHERE  e.name = :username AND tr.time_from BETWEEN :start AND :end
            GROUP BY e.name, p.name
            ORDER BY e.name, p.name
            """, nativeQuery = true)
    List<Object[]> findReportsByEmployeeUsername(@Param("username") String username,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}
