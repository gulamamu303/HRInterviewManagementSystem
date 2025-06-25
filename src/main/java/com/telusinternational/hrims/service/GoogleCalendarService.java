package com.telusinternational.hrims.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.ConferenceSolutionKey;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleCalendarService {
    private static final String APPLICATION_NAME = "HR Interview Management System";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    @Value("${google.calendar.id}")
    private String calendarId;

    @Autowired
    private GoogleAuthorizationCodeFlow flow;

    private static final String USER_ID = "user";

    public void setRefreshToken(String refreshToken) throws IOException {
        System.out.println("Saving refresh token: " + (refreshToken != null && !refreshToken.isBlank()));
        GoogleTokenResponse tokenResponse = new GoogleTokenResponse().setRefreshToken(refreshToken);
        flow.createAndStoreCredential(tokenResponse, USER_ID);
    }

    private Credential getCredentials() throws IOException {
        Credential credential = flow.loadCredential(USER_ID);
        System.out.println("getCredentials: credential is " + (credential != null ? "NOT null" : "null"));
        System.out.println("getCredentials: refresh token is " + (credential != null && credential.getRefreshToken() != null));
        if (credential == null || credential.getRefreshToken() == null) {
            throw new IOException("No refresh token available. Please authorize first.");
        }
        return credential;
    }

    public String createInterviewEvent(String summary, String description, LocalDateTime startDateTime,
                                     LocalDateTime endDateTime, List<String> attendees) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credentials = getCredentials();
        
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(
                        startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(
                        endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        event.setEnd(end);

        List<EventAttendee> eventAttendees = attendees.stream()
                .map(email -> new EventAttendee().setEmail(email))
                .toList();
        event.setAttendees(eventAttendees);

        Event createdEvent = null;
        try {
            createdEvent = service.events().insert(calendarId, event)
                    .setSendUpdates("all")
                    .execute();
        } catch (IOException e) {
            System.out.println("Failed to insert event: " + e.getMessage());
        }

        return createdEvent.getHtmlLink();
    }

    public Event createInterviewEventAndReturnEvent(String summary, String description, LocalDateTime startDateTime,
                                                   LocalDateTime endDateTime, List<String> attendees) throws IOException {
        final NetHttpTransport HTTP_TRANSPORT;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            throw new IOException("Failed to initialize HTTP transport", e);
        }
        Credential credentials = getCredentials();

        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(
                        startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(
                        endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        event.setEnd(end);

        List<EventAttendee> eventAttendees = attendees.stream()
                .map(email -> new EventAttendee().setEmail(email))
                .toList();
        event.setAttendees(eventAttendees);

        Event createdEvent = null;
        try {
            createdEvent = service.events().insert(calendarId, event)
                    .setSendUpdates("all")
                    .execute();
        } catch (IOException e) {
            System.out.println("Failed to insert event: " + e.getMessage());
            throw e;
        }

        return createdEvent;
    }

    public void deleteEvent(String eventId) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Credential credentials = getCredentials();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            service.events().delete(calendarId, eventId).execute();
        } catch (Exception e) {
            System.out.println("Failed to delete event from Google Calendar: " + e.getMessage());
        }
    }

    public void updateEvent(String eventId, String summary, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> attendees) throws IOException {
        final NetHttpTransport HTTP_TRANSPORT;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            throw new IOException("Failed to initialize HTTP transport", e);
        }
        Credential credentials = getCredentials();

        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = service.events().get(calendarId, eventId).execute();
        event.setSummary(summary);
        event.setDescription(description);

        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        event.setEnd(end);

        List<EventAttendee> eventAttendees = attendees.stream()
                .map(email -> new EventAttendee().setEmail(email))
                .toList();
        event.setAttendees(eventAttendees);

        service.events().update(calendarId, eventId, event)
                .setSendUpdates("all")
                .execute();
    }
} 