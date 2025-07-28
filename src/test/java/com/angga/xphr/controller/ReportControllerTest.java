package com.angga.xphr.controller;

import com.angga.xphr.model.dto.ReportDTO;
import com.angga.xphr.service.ReportService;
import com.angga.xphr.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "angga", roles = "ADMIN")
    void testGetReportAsAdmin() throws Exception {
        when(reportService.getReport(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(new ReportDTO("Angga", "Xphr", 10.0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/report"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("report"))
                .andExpect(view().name("report"));
    }

    @Test
    @WithMockUser(username = "angga", roles = "EMPLOYEE")
    void testGetReportAsUser() throws Exception {
        when(reportService.getReportForEmployee(Mockito.eq("angga"), Mockito.any(), Mockito.any()))
                .thenReturn(List.of(new ReportDTO("Angga", "Xphr", 8.0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/report"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("report"))
                .andExpect(view().name("report"));
    }
}
