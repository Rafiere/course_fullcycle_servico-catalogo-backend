package com.projetopraticobackend.servicocatalogo.application.category.retrieve.get;

import com.projetopraticobackend.servicocatalogo.IntegrationTest;
import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;
import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory(){

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = category.getId();

        save(category);

        final var savedCategory = getCategoryByIdUseCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, savedCategory.id());
        Assertions.assertEquals(expectedName, savedCategory.name());
        Assertions.assertEquals(expectedDescription, savedCategory.description());
        Assertions.assertEquals(expectedIsActive, savedCategory.isActive());

        Assertions.assertEquals(category.getDeletedAt(), savedCategory.deletedAt());
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCategory_shouldReturnNotFound(){

        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        final var exception = Assertions.assertThrows(DomainException.class,
                () -> getCategoryByIdUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from("123");

        /* Estamos utilizando o "doThrow()" porque estamos lidando com um "SpyBean". */
        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).findById(Mockito.eq(expectedId));

        final var exception = Assertions.assertThrows(IllegalStateException.class,
                () -> getCategoryByIdUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }

    private void save(final Category... categories){

        final List<CategoryJpaEntity> categoriesEntities = Arrays.stream(categories)
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categoriesEntities);
    }
}