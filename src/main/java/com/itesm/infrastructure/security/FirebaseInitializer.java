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
                log.info("--- INICIANDO DIAGNÓSTICO DE FIREBASE ---");
                log.info("Variable firebase.service-account-location configurada como: '{}'", serviceAccountLocation);

                FirebaseOptions.Builder optionsBuilder = FirebaseOptions.builder();
                InputStream serviceAccount = null;

                if (!"none".equalsIgnoreCase(serviceAccountLocation)) {
                    log.info(
                            "Intentando inicializar Firebase usando el archivo especificado en la variable de entorno...");
                    try {
                        serviceAccount = new FileInputStream(serviceAccountLocation);
                        log.info("Archivo {} encontrado correctamente.", serviceAccountLocation);
                    } catch (Exception e) {
                        log.error("ERROR CRÍTICO: No se pudo abrir el archivo especificado en la ruta: {}",
                                serviceAccountLocation, e);
                    }
                } else {
                    log.info(
                            "La variable es 'none', intentando buscar 'todolist.json' en los recursos del classpath (empaquetado en el JAR)...");
                    serviceAccount = Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream("todolist.json");
                    if (serviceAccount != null) {
                        log.info("Archivo 'todolist.json' encontrado exitosamente en el classpath.");
                    } else {
                        log.warn("ATENCIÓN: No se encontró 'todolist.json' en el classpath.");
                    }
                }

                if (serviceAccount != null) {
                    try {
                        optionsBuilder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
                        log.info("Credenciales leídas desde el archivo de configuración (JSON).");
                    } finally {
                        serviceAccount.close();
                    }
                } else {
                    log.warn(
                            "No se pudo obtener el archivo de credenciales. Intentando usar Google Application Default Credentials (ADC) para Cloud Run/App Engine...");
                    try {
                        optionsBuilder.setCredentials(GoogleCredentials.getApplicationDefault());
                        log.info("Google Application Default Credentials cargadas exitosamente.");
                    } catch (Exception e) {
                        log.error(
                                "No se pudieron cargar las credenciales por defecto de Google (ADC). Firebase NO se inicializará correctamente.",
                                e);
                        return;
                    }
                }

                FirebaseApp.initializeApp(optionsBuilder.build());
                log.info("Firebase application inicializada correctamente.");
                log.info("--- FIN DIAGNÓSTICO DE FIREBASE ---");
            }
        } catch (Exception e) {
            log.error("Error general inicializando Firebase App", e);
        }
    }
}
