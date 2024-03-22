package com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence;

/* Estamos utilizando o "JpaEntity" porque essa é a entidade do JPA, e não uma entidade de domínio. */

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity //Essa anotação diz que essa entidade será utilizada para persistência.
@Table(name = "category")
public class CategoryJpaEntity {

    @Id //Esse atributo será utilizado como o identificador do ID.
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public CategoryJpaEntity() {}

    /* Receberemos um agregado e converteremos esse agregado para uma entidade JPA. */
    public static CategoryJpaEntity from(final Category category){
        return new CategoryJpaEntity(
                category.getId().getValue(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt());
    }

    /* Esse é o caminho inverso. Ele serve para convertermos uma entidade em um agregado. */
    public static Category toAggregate(final CategoryJpaEntity categoryJpaEntity){
        return Category.with(
                CategoryID.from(categoryJpaEntity.getId()),
                categoryJpaEntity.getName(),
                categoryJpaEntity.getDescription(),
                categoryJpaEntity.isActive(),
                categoryJpaEntity.getCreatedAt(),
                categoryJpaEntity.getUpdatedAt(),
                categoryJpaEntity.getDeletedAt());
    }

    private CategoryJpaEntity(final String id,
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}