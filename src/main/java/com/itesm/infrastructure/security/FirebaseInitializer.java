package com.itesm.infrastructure.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;

@ApplicationScoped
public class FirebaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(FirebaseInitializer.class);

    @ConfigProperty(name = "firebase.service-account-location", defaultValue = "none")
    String serviceAccountLocation;

    void onStart(@Observes StartupEvent ev) {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions.Builder optionsBuilder = FirebaseOptions.builder();

                InputStream serviceAccount = null;

                if (!"none".equalsIgnoreCase(serviceAccountLocation)) {
                    log.info("Initializing Firebase with service account at: {}", serviceAccountLocation);
                    serviceAccount = new FileInputStream(serviceAccountLocation);
                } else {
                    log.info("Checking classpath for todolist.json...");
                    serviceAccount = Thread.currentThread().getContextClassLoader().getResourceAsStream("todolist.json");
                }

                if (serviceAccount == null) {
                    log.warn("No Firebase service account provided or found. Skipping real Firebase initialization.");
                    return;
                }

                try {
                    optionsBuilder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
                } finally {
                    serviceAccount.close();
                }

                FirebaseApp.initializeApp(optionsBuilder.build());
                log.info("Firebase application initialized successfully.");
            }
        } catch (Exception e) {
            log.error("Error initializing Firebase App", e);
        }
    }
}
