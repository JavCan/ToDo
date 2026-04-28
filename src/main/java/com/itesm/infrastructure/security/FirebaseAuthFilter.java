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

import java.io.IOException;
import java.util.Optional;

import io.quarkus.arc.profile.UnlessBuildProfile;

@Provider
@Priority(Priorities.AUTHENTICATION)
@UnlessBuildProfile("test")
public class FirebaseAuthFilter implements ContainerRequestFilter {
    @Inject
    UserRepository userRepository;
    @Inject
    AuthenticatedUserContext authenticatedUserContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        // Integración de rutas ignoradas de todas las ramas
        if (path.equals("/user") || path.equals("/status") || path.startsWith("/todo/demo")) {
            return;
        }

        String authHeader = requestContext.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.err.println("FirebaseAuthFilter: Missing or invalid Authorization header. Received: " + authHeader);
            requestContext.abortWith(Response.status(401).build());
            return;
        }

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(authHeader.replace("Bearer ", ""),
                    true);
            Optional<User> userOptional = userRepository.findByFirebaseUuid(decodedToken.getUid());

            if (userOptional.isEmpty()) {
                System.out.println("FirebaseAuthFilter: User not found in DB with Firebase UID: " + decodedToken.getUid());
                requestContext.abortWith(Response.status(401).build());
                return;
            }

            User user = userOptional.get();
            CurrentUser currentUser = new CurrentUser(
                    user.getId(), user.getFirebaseUuid(), user.getEmail(), user.getRole(), user.getFullName());
            authenticatedUserContext.setCurrentUser(currentUser);
        } catch (FirebaseAuthException e) {
            System.err.println("FirebaseAuthFilter: Token verification failed!");
            e.printStackTrace();
            requestContext.abortWith(Response.status(401).build());
        }
    }
}