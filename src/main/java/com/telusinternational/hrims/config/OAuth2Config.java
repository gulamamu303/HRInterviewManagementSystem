package com.telusinternational.hrims.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.client.util.store.FileDataStoreFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

@Configuration
public class OAuth2Config {

    @Value("${google.oauth2.credentials.file}")
    private String oauth2CredentialsFile;

    @Bean
    public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow() throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                GsonFactory.getDefaultInstance(),
                new InputStreamReader(new FileInputStream(oauth2CredentialsFile))
        );

        // Use a persistent file-based data store
        FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(new java.io.File("tokens"));

        return new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR)
        )
                .setDataStoreFactory(dataStoreFactory)
                .setAccessType("offline")
                .build();
    }
} 