package com.projetopraticobackend.servicocatalogo.application.category.update;

import com.projetopraticobackend.servicocatalogo.IntegrationTest;
import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
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

/* Esses são os mesmos testes unitários feitos nos casos de uso, porém, aqui estamos acessando realmente o banco de
* dados, ao invés de apenas realizar um mock. */

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGatewaySpy;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){

        //Abaixo, temos a categoria que será atualizada.
        final var categoryToBeUpdated = Category.newCategory("Film", null, true);

        save(categoryToBeUpdated);

        //Abaixo, temos os valores esperados para a categoria após a atualização.
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = categoryToBeUpdated.getId();

        //Estamos garantindo que existe apenas uma categoria salva no banco.
        Assertions.assertEquals(1, categoryRepository.count());

        final var expectedCategoryCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        final var updatedCategory = updateCategoryUseCase.execute(expectedCategoryCommand).get();

        Assertions.assertNotNull(updatedCategory);
        Assertions.assertNotNull(updatedCategory.id());

        final var updatedCategoryFound = categoryRepository.findById(updatedCategory.id().getValue()).get();

        Assertions.assertEquals(expectedName, updatedCategoryFound.getName());
        Assertions.assertEquals(expectedDescription, updatedCategoryFound.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategoryFound.isActive());

        Assertions.assertNotNull(updatedCategoryFound.getUpdatedAt());
        Assertions.assertTrue(updatedCategoryFound.getCreatedAt().isBefore(categoryToBeUpdated.getUpdatedAt()));
        Assertions.assertNull(updatedCategoryFound.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException(){

        final var category = Category.newCategory("Filmes", null, true);

        save(category);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var notification = updateCategoryUseCase.execute(
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive))
                .getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGatewaySpy, Mockito.never()).update(Mockito.any());
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategory(){

        final var categoryToBeUpdated = Category.newCategory("Film", null, true);

        save(categoryToBeUpdated);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = categoryToBeUpdated.getId();

        Assertions.assertTrue(categoryToBeUpdated.isActive());
        Assertions.assertNull(categoryToBeUpdated.getDeletedAt());

        final var updatedCategory = updateCategoryUseCase.execute(
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive))
                .get();

        Assertions.assertNotNull(updatedCategory);
        Assertions.assertNotNull(updatedCategory.id());

        final var updatedCategoryFound = categoryRepository.findById(updatedCategory.id().getValue()).get();

        Assertions.assertEquals(expectedName, updatedCategoryFound.getName());
        Assertions.assertEquals(expectedDescription, updatedCategoryFound.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategoryFound.isActive());

        Assertions.assertNotNull(updatedCategoryFound.getUpdatedAt());
        Assertions.assertTrue(updatedCategoryFound.getCreatedAt().isBefore(categoryToBeUpdated.getUpdatedAt()));
        Assertions.assertNotNull(updatedCategoryFound.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){

        final var categoryToBeUpdated = Category.newCategory("Film", null, true);

        save(categoryToBeUpdated);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = categoryToBeUpdated.getId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGatewaySpy)
                .update(Mockito.any());

        final var notification = updateCategoryUseCase.execute(
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive))
                .getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        final var originalCategoryFound = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(categoryToBeUpdated.getName(), originalCategoryFound.getName());
        Assertions.assertEquals(categoryToBeUpdated.getDescription(), originalCategoryFound.getDescription());
        Assertions.assertEquals(categoryToBeUpdated.isActive(), originalCategoryFound.isActive());

        Assertions.assertNotNull(originalCategoryFound.getUpdatedAt());
        Assertions.assertNull(originalCategoryFound.getDeletedAt());
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException(){

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = "123";

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> updateCategoryUseCase.execute(command));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }


    private void save(final Category... categories){

        final List<CategoryJpaEntity> categoriesEntities = Arrays.stream(categories)
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categoriesEntities);
    }
}