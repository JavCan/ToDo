package com.itesm.infrastructure.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import io.quarkus.arc.profile.UnlessBuildProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@Provider
@Priority(Priorities.AUTHENTICATION)
@UnlessBuildProfile("test")
public class FirebaseAuthFilter implements ContainerRequestFilter {
    
    private static final Logger log = LoggerFactory.getLogger(FirebaseAuthFilter.class);

    @Inject
    UserRepository userRepository;

    @Inject
    AuthenticatedUserContext authenticatedUserContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        
        // Rutas ignoradas
        if (path.equals("/user") || path.equals("/status") || path.startsWith("/todo/demo")) {
            return;
        }

        String authHeader = requestContext.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("FirebaseAuthFilter: Missing or invalid Authorization header. Path: {}", path);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Missing or invalid Authorization header\"}")
                    .build());
            return;
        }

        String token = authHeader.substring(7);

        try {
            log.debug("Verificando token de Firebase para el path: {}", path);
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token, true);
            String uid = decodedToken.getUid();
            log.info("Token de Firebase verificado. UID: {}", uid);

            log.debug("Buscando usuario en la base de datos local para el UID: {}", uid);
            Optional<User> userOptional = userRepository.findByFirebaseUuid(uid);

            if (userOptional.isEmpty()) {
                log.error("FirebaseAuthFilter: Usuario con UID {} no existe en la base de datos local.", uid);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"User not found in local database. UID: " + uid + "\"}")
                        .build());
                return;
            }

            User user = userOptional.get();
            CurrentUser currentUser = new CurrentUser(
                    user.getId(), user.getFirebaseUuid(), user.getEmail(), user.getRole(), user.getFullName());
            
            authenticatedUserContext.setCurrentUser(currentUser);
            log.info("Autenticación exitosa para el usuario: {}", user.getEmail());

        } catch (FirebaseAuthException e) {
            log.error("FirebaseAuthFilter: Token verification failed: {}", e.getMessage());
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid Firebase token\", \"details\": \"" + e.getMessage() + "\"}")
                    .build());
        } catch (Exception e) {
            log.error("FirebaseAuthFilter: Error inesperado durante la autenticación", e);
            requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Internal server error during authentication\", \"details\": \"" + e.getMessage() + "\"}")
                    .build());
        }
    }
}