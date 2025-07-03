package com.telusinternational.hrims.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telusinternational.hrims.entity.InterviewSchedule;
import com.telusinternational.hrims.exception.InvalidDateRangeException;
import com.telusinternational.hrims.service.InterviewScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InterviewScheduleController.class)
public class InterviewScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterviewScheduleService interviewScheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllInterviews_whenNoParams_shouldReturnOkAndAllInterviews() throws Exception {
        List<InterviewSchedule> schedules = new ArrayList<>();
        // Add mock data if needed
        when(interviewScheduleService.getAllInterviews(null, null)).thenReturn(schedules);

        mockMvc.perform(get("/schedule"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllInterviews_withValidDateParams_shouldReturnOkAndFilteredInterviews() throws Exception {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 10);
        List<InterviewSchedule> schedules = new ArrayList<>(); // Mock filtered list
        when(interviewScheduleService.getAllInterviews(startDate, endDate)).thenReturn(schedules);

        mockMvc.perform(get("/schedule")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllInterviews_withInvalidDateRange_shouldReturnNotFound() throws Exception {
        LocalDate startDate = LocalDate.of(2024, 1, 10);
        LocalDate endDate = LocalDate.of(2024, 1, 1);
        String errorMessage = "Invalid input- End Date should be grater than start date";

        when(interviewScheduleService.getAllInterviews(startDate, endDate))
                .thenThrow(new InvalidDateRangeException(errorMessage));

        ResultActions resultActions = mockMvc.perform(get("/schedule")
                        .param("startDate", "2024-01-10")
                        .param("endDate", "2024-01-01"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void getAllInterviews_withEndDateEqualToStartDate_shouldReturnNotFound() throws Exception {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 1);
        String errorMessage = "Invalid input- End Date should be grater than start date";

        when(interviewScheduleService.getAllInterviews(startDate, endDate))
                .thenThrow(new InvalidDateRangeException(errorMessage));

        ResultActions resultActions = mockMvc.perform(get("/schedule")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-01-01"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void getAllInterviews_withOnlyStartDateParam_shouldReturnOk() throws Exception {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        List<InterviewSchedule> schedules = new ArrayList<>();
        when(interviewScheduleService.getAllInterviews(startDate, null)).thenReturn(schedules);

        mockMvc.perform(get("/schedule")
                        .param("startDate", "2024-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllInterviews_withOnlyEndDateParam_shouldReturnOk() throws Exception {
        LocalDate endDate = LocalDate.of(2024, 1, 10);
        List<InterviewSchedule> schedules = new ArrayList<>();
        when(interviewScheduleService.getAllInterviews(null, endDate)).thenReturn(schedules);

        mockMvc.perform(get("/schedule")
                        .param("endDate", "2024-01-10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllInterviews_withInvalidDateFormat_shouldReturnBadRequest() throws Exception {
        // Spring Boot's default behavior with @DateTimeFormat is to return a 400 Bad Request
        // if the date format is incorrect. We don't need to mock the service for this.
        mockMvc.perform(get("/schedule")
                        .param("startDate", "invalid-date-format")
                        .param("endDate", "2024-01-10"))
                .andExpect(status().isBadRequest());
    }
}
