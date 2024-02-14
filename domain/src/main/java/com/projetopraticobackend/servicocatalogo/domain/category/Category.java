package com.projetopraticobackend.servicocatalogo.domain.category;

import java.time.Instant;
import java.util.UUID;

public class Category {

    private String id;
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt; //A semântica do "Instant" é um marco no tempo, logo, ele calcula o tempo desde o início e conta os segundos até aqui. Ele é melhor do que o LocalDateTime pois ele é mais preciso e não depende do fuso horário, pois é sempre UTC.
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final String id,
                    final String name,
                    final String description,
                    final boolean active,
                    final Instant createdAt,
                    final Instant updatedAt,
                    final Instant deletedAt) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    //Esse método servirá como a "factory" para criar uma nova categoria.
    public static Category newCategory(final String name,
                                       final String description,
                                       final boolean active) {

        final var id = UUID.randomUUID().toString();
        final var now = Instant.now();

        return new Category(id,
                name,
                description,
                active,
                now,
                now,
                null);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}