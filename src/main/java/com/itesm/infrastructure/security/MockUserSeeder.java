package com.itesm.infrastructure.security;

import com.itesm.infrastructure.persistence.entity.UserEntity;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class MockUserSeeder {

    @Inject
    EntityManager em;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        UUID mockId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // Si el usuario no existe, lo creamos
        if (em.find(UserEntity.class, mockId) == null) {
            UserEntity u = new UserEntity(
                    mockId, "Test User", "test@test.com", true,
                    "mock-firebase-uid", LocalDateTime.now(), LocalDateTime.now(), "USER");
            em.persist(u);
        }
    }
}