package com.telusinternational.hrims.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.telusinternational.hrims.service.OAuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    private GoogleAuthorizationCodeFlow flow;

    @Autowired
    private OAuthTokenService oAuthTokenService;

    @GetMapping("/authorize")
    public ResponseEntity<String> authorize() {
        String authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri("http://localhost:8080/hrims/v1/oauth2/callback")
                .setAccessType("offline")
                .set("prompt", "consent")
                .build();
        return ResponseEntity.ok("Please visit this URL to authorize: " + authorizationUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        try {
            System.out.println("Received authorization code: " + code);
            
            GoogleTokenResponse response = flow.newTokenRequest(code)
                    .setRedirectUri("http://localhost:8080/hrims/v1/oauth2/callback")
                    .execute();

            // Store the refresh token
            String refreshToken = response.getRefreshToken();
            System.out.println("Received refresh token: " + (refreshToken != null ? "YES" : "NO"));
            
            if (refreshToken == null) {
                return ResponseEntity.badRequest().body("No refresh token received. Please try again.");
            }
            
            oAuthTokenService.setRefreshToken(refreshToken);
            
            return ResponseEntity.ok("Authorization successful! You can now schedule interviews.");
        } catch (IOException e) {
            System.err.println("OAuth callback error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Authorization failed: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        boolean isAuthorized = oAuthTokenService.isAuthorized();
        if (isAuthorized) {
            return ResponseEntity.ok("OAuth2 is authorized and ready to use.");
        } else {
            return ResponseEntity.ok("OAuth2 is not authorized. Please visit /oauth2/authorize first.");
        }
    }
} 