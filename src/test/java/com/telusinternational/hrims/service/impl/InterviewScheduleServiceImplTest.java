package com.telusinternational.hrims.service.impl;

import com.telusinternational.hrims.entity.InterviewSchedule;
import com.telusinternational.hrims.entity.InterviewSchedule.CalendarInfo;
import com.telusinternational.hrims.repository.InterviewScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewScheduleServiceImplTest {

    @Mock
    private InterviewScheduleRepository interviewScheduleRepository;

    @InjectMocks
    private InterviewScheduleServiceImpl interviewScheduleService;

    @Test
    void testGetAllInterviews_noDatesProvided() {
        InterviewSchedule schedule1 = new InterviewSchedule();
        schedule1.setId(1L);
        CalendarInfo calendarInfo1 = new CalendarInfo();
        calendarInfo1.setDate(LocalDate.of(2024, 7, 20));
        schedule1.setCalendarInfo(calendarInfo1);

        InterviewSchedule schedule2 = new InterviewSchedule();
        schedule2.setId(2L);
        CalendarInfo calendarInfo2 = new CalendarInfo();
        calendarInfo2.setDate(LocalDate.of(2024, 7, 21));
        schedule2.setCalendarInfo(calendarInfo2);

        List<InterviewSchedule> expectedSchedules = Arrays.asList(schedule1, schedule2);

        when(interviewScheduleRepository.findAll()).thenReturn(expectedSchedules);

        List<InterviewSchedule> actualSchedules = interviewScheduleService.getAllInterviews(null, null);

        assertEquals(expectedSchedules.size(), actualSchedules.size());
        assertEquals(expectedSchedules, actualSchedules);
        verify(interviewScheduleRepository, times(1)).findAll();
        verify(interviewScheduleRepository, never()).findByCalendarInfo_DateBetween(any(), any());
    }

    @Test
    void testGetAllInterviews_withDatesProvided() {
        LocalDate startDate = LocalDate.of(2024, 7, 20);
        LocalDate endDate = LocalDate.of(2024, 7, 22);

        InterviewSchedule schedule1 = new InterviewSchedule();
        schedule1.setId(1L);
        CalendarInfo calendarInfo1 = new CalendarInfo();
        calendarInfo1.setDate(LocalDate.of(2024, 7, 20));
        schedule1.setCalendarInfo(calendarInfo1);

        InterviewSchedule schedule2 = new InterviewSchedule();
        schedule2.setId(2L);
        CalendarInfo calendarInfo2 = new CalendarInfo();
        calendarInfo2.setDate(LocalDate.of(2024, 7, 21));
        schedule2.setCalendarInfo(calendarInfo2);
        List<InterviewSchedule> expectedSchedules = Arrays.asList(schedule1, schedule2);

        when(interviewScheduleRepository.findByCalendarInfo_DateBetween(startDate, endDate)).thenReturn(expectedSchedules);

        List<InterviewSchedule> actualSchedules = interviewScheduleService.getAllInterviews(startDate, endDate);

        assertEquals(expectedSchedules.size(), actualSchedules.size());
        assertEquals(expectedSchedules, actualSchedules);
        verify(interviewScheduleRepository, times(1)).findByCalendarInfo_DateBetween(startDate, endDate);
        verify(interviewScheduleRepository, never()).findAll();
    }

    @Test
    void testGetInterviewsByDateRange_success() {
        LocalDate startDate = LocalDate.of(2024, 7, 20);
        LocalDate endDate = LocalDate.of(2024, 7, 22);

        InterviewSchedule schedule1 = new InterviewSchedule();
        schedule1.setId(1L);
        CalendarInfo calendarInfo1 = new CalendarInfo();
        calendarInfo1.setDate(LocalDate.of(2024, 7, 20));
        schedule1.setCalendarInfo(calendarInfo1);
        List<InterviewSchedule> expectedSchedules = Collections.singletonList(schedule1);

        when(interviewScheduleRepository.findByCalendarInfo_DateBetween(startDate, endDate)).thenReturn(expectedSchedules);

        List<InterviewSchedule> actualSchedules = interviewScheduleService.getInterviewsByDateRange(startDate, endDate);

        assertEquals(expectedSchedules.size(), actualSchedules.size());
        assertEquals(expectedSchedules, actualSchedules);
        verify(interviewScheduleRepository, times(1)).findByCalendarInfo_DateBetween(startDate, endDate);
    }

    @Test
    void testGetInterviewsByDateRange_noResults() {
        LocalDate startDate = LocalDate.of(2024, 8, 1);
        LocalDate endDate = LocalDate.of(2024, 8, 5);

        when(interviewScheduleRepository.findByCalendarInfo_DateBetween(startDate, endDate)).thenReturn(Collections.emptyList());

        List<InterviewSchedule> actualSchedules = interviewScheduleService.getInterviewsByDateRange(startDate, endDate);

        assertEquals(0, actualSchedules.size());
        verify(interviewScheduleRepository, times(1)).findByCalendarInfo_DateBetween(startDate, endDate);
    }
}
