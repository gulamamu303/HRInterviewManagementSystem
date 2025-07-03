package com.telusinternational.hrims.service.impl;

import com.telusinternational.hrims.entity.InterviewSchedule;
import com.telusinternational.hrims.exception.InvalidDateRangeException;
import com.telusinternational.hrims.repository.InterviewScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewScheduleServiceImplTest {

    @Mock
    private InterviewScheduleRepository interviewScheduleRepository;

    @InjectMocks
    private InterviewScheduleServiceImpl interviewScheduleService;

    @Test
    void getAllInterviews_whenBothDatesProvided_andValid_shouldReturnFilteredList() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        List<InterviewSchedule> expectedSchedules = new ArrayList<>();
        // Add some mock schedules to expectedSchedules if needed for verification

        when(interviewScheduleRepository.findByCalendarInfo_DateBetween(startDate, endDate)).thenReturn(expectedSchedules);

        List<InterviewSchedule> actualSchedules = interviewScheduleService.getAllInterviews(startDate, endDate);

        assertEquals(expectedSchedules, actualSchedules);
        verify(interviewScheduleRepository).findByCalendarInfo_DateBetween(startDate, endDate);
        verify(interviewScheduleRepository, never()).findAll();
    }

    @Test
    void getAllInterviews_whenBothDatesProvided_andEndDateBeforeStartDate_shouldThrowException() {
        LocalDate startDate = LocalDate.of(2024, 1, 31);
        LocalDate endDate = LocalDate.of(2024, 1, 1);

        InvalidDateRangeException exception = assertThrows(InvalidDateRangeException.class, () -> {
            interviewScheduleService.getAllInterviews(startDate, endDate);
        });

        assertEquals("Invalid input- End Date should be grater than start date", exception.getMessage());
        verify(interviewScheduleRepository, never()).findByCalendarInfo_DateBetween(any(), any());
        verify(interviewScheduleRepository, never()).findAll();
    }

    @Test
    void getAllInterviews_whenBothDatesProvided_andEndDateEqualsStartDate_shouldThrowException() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 1);

        InvalidDateRangeException exception = assertThrows(InvalidDateRangeException.class, () -> {
            interviewScheduleService.getAllInterviews(startDate, endDate);
        });

        assertEquals("Invalid input- End Date should be grater than start date", exception.getMessage());
        verify(interviewScheduleRepository, never()).findByCalendarInfo_DateBetween(any(), any());
        verify(interviewScheduleRepository, never()).findAll();
    }

    @Test
    void getAllInterviews_whenOnlyStartDateProvided_shouldReturnAll() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        List<InterviewSchedule> expectedSchedules = new ArrayList<>();
        when(interviewScheduleRepository.findAll()).thenReturn(expectedSchedules);

        List<InterviewSchedule> actualSchedules = interviewScheduleService.getAllInterviews(startDate, null);

        assertEquals(expectedSchedules, actualSchedules);
        verify(interviewScheduleRepository).findAll();
        verify(interviewScheduleRepository, never()).findByCalendarInfo_DateBetween(any(), any());
    }

    @Test
    void getAllInterviews_whenOnlyEndDateProvided_shouldReturnAll() {
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        List<InterviewSchedule> expectedSchedules = new ArrayList<>();
        when(interviewScheduleRepository.findAll()).thenReturn(expectedSchedules);

        List<InterviewSchedule> actualSchedules = interviewScheduleService.getAllInterviews(null, endDate);

        assertEquals(expectedSchedules, actualSchedules);
        verify(interviewScheduleRepository).findAll();
        verify(interviewScheduleRepository, never()).findByCalendarInfo_DateBetween(any(), any());
    }

    @Test
    void getAllInterviews_whenNoDatesProvided_shouldReturnAll() {
        List<InterviewSchedule> expectedSchedules = new ArrayList<>();
        when(interviewScheduleRepository.findAll()).thenReturn(expectedSchedules);

        List<InterviewSchedule> actualSchedules = interviewScheduleService.getAllInterviews(null, null);

        assertEquals(expectedSchedules, actualSchedules);
        verify(interviewScheduleRepository).findAll();
        verify(interviewScheduleRepository, never()).findByCalendarInfo_DateBetween(any(), any());
    }
}
