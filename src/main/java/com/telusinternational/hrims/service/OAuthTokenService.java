package com.telusinternational.hrims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OAuthTokenService {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    public void setRefreshToken(String refreshToken) throws IOException {
        googleCalendarService.setRefreshToken(refreshToken);
    }

    public boolean isAuthorized() {
        try {
            // Try to get credentials to check if authorized
            googleCalendarService.createInterviewEvent(
                "Test", "Test", 
                java.time.LocalDateTime.now(), 
                java.time.LocalDateTime.now().plusHours(1), 
                java.util.List.of("test@example.com")
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 