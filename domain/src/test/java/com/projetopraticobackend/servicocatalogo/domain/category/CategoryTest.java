package com.projetopraticobackend.servicocatalogo.domain.category;

import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import com.projetopraticobackend.servicocatalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    /* Utilizaremos a nomenclatura "given_when_then". */

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory(){

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());

        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    /* O teste abaixo fará o teste utilizando o "ThrowsValidationHandler", ao invés do "NotificationHandler" ou
    * seja, se um erro ocorrer, ele será lançado imediatamente ao invés de acumular. */
    @Test
    public void givenAInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError(){

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        /* Esperamos receber uma "DomainException" pois o nome não pode ser nulo. */
        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    /* O teste abaixo fará o teste utilizando o "ThrowsValidationHandler", ao invés do "NotificationHandler" ou
     * seja, se um erro ocorrer, ele será lançado imediatamente ao invés de acumular. */
    @Test
    public void givenAInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError(){

        final String expectedName = "  ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        /* Esperamos receber uma "DomainException" pois o nome não pode ser vazio. */
        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    /* O teste abaixo fará o teste utilizando o "ThrowsValidationHandler", ao invés do "NotificationHandler" ou
     * seja, se um erro ocorrer, ele será lançado imediatamente ao invés de acumular. */
    @Test
    public void givenAInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError(){

        final String expectedName = "Fi ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        /* Esperamos receber uma "DomainException" pois o nome não pode ser menor do que 3 caracteres. */
        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    /* O teste abaixo fará o teste utilizando o "ThrowsValidationHandler", ao invés do "NotificationHandler" ou
     * seja, se um erro ocorrer, ele será lançado imediatamente ao invés de acumular. */
    @Test
    public void givenAInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError(){

        final String expectedName =
                """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. In risus nibh, semper 
                at porta tristique, laoreet in nunc. Maecenas iaculis massa id nisl consectetur ornare. Praesent 
                condimentum, felis a varius lacinia, mi nibh feugiat odio, id fringilla libero quam sed sapien. 
                Aenean hendrerit odio vel dui hendrerit, sit amet tempor nisl aliquet.
                """;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        /* Esperamos receber uma "DomainException" pois o nome não pode ser maior do que 255 caracteres. */
        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    /* O teste abaixo fará o teste utilizando o "ThrowsValidationHandler", ao invés do "NotificationHandler" ou
     * seja, se um erro ocorrer, ele será lançado imediatamente ao invés de acumular. */
    @Test
    public void givenAValidEmptyDescription_whenCallNewCategoryAndValidate_thenInstantiateACategory(){

        final var expectedName = "Filmes";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());

        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidFalseIsActive_whenCallNewCategoryAndValidate_thenInstantiateACategory(){

        final var expectedName = "Filmes";
        final var expectedDescription = "";
        final var expectedIsActive = false;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());

        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }
}
