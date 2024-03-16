package com.projetopraticobackend.servicocatalogo.application.category.update;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;
import com.projetopraticobackend.servicocatalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase updateCategoryUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach //Esse método será executado antes de cada teste.
    void cleanUp(){
        Mockito.reset(categoryGateway); //Estamos garantindo que o "categoryGateway" estará limpo antes de cada teste, assim, garantimos que nenhum comportamento está sendo passado, de forma indevida, de um teste para o outro.
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){

        //Arrange

        final var initialCategory = Category.newCategory("Film", null, true);

        final var expectedId = initialCategory.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var command = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        //Precisamos mockar o comportamento do teste.

        //Teremos duas chamadas para o gateway. O "find" para buscar a categoria e o "update" para atualizar a categoria na camada de persistência.

        //Quando chamarmos o "findById" chamando o "expectedId", retornaremos um "Optional" com a categoria que foi criada inicialmente.
        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.clone(initialCategory))); //Estamos retornando um objeto com os mesmos atributos, mas com referências diferentes.

        //Esse método retornará o que for recebido como parâmetro do método.
        Mockito.when(categoryGateway.update(Mockito.any(Category.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //Act
        final var actualOutput = updateCategoryUseCase.execute(command).get();

        //Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        //Estamos garantindo que o "findById()", com o argumento "expectedId", foi chamado uma única vez.
        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        //Estamos garantindo que o "update()", com o argumento "category", foi chamado uma única vez.
        Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(
                updatedCategory -> //Estamos garantindo que o objeto passado para o método "update()" como argumento, que é o "updatedCategory" e, logo, é o objeto retornado por esse método, pois estamos utilizando o "returnsFirstArg()", é igual ao objeto que esperamos que seja retornado, que é o objeto com os valores atualizados.
                        Objects.equals(expectedId, updatedCategory.getId()) &&
                        Objects.equals(expectedName, updatedCategory.getName()) &&
                        Objects.equals(expectedDescription, updatedCategory.getDescription()) &&
                        Objects.equals(expectedIsActive, updatedCategory.isActive()) &&
                        (initialCategory.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())) &&
                        Objects.isNull(updatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException(){

        //Arrange
        final var initialCategory = Category.newCategory("Film", null, true);

        final var expectedId = initialCategory.getId();
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.clone(initialCategory)));

        //Act
        final var notification = updateCategoryUseCase.execute(command).getLeft();

        //Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(categoryGateway, Mockito.never()).update(Mockito.any(Category.class));
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsCreateCategory_shouldReturnInactiveCategoryId(){

        //Arrange
        final var initialCategory = Category.newCategory("Film", null, true);

        final var expectedId = initialCategory.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var command = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        //Precisamos mockar o comportamento do teste.

        //Teremos duas chamadas para o gateway. O "find" para buscar a categoria e o "update" para atualizar a categoria na camada de persistência.

        //Quando chamarmos o "findById" chamando o "expectedId", retornaremos um "Optional" com a categoria que foi criada inicialmente.
        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.clone(initialCategory))); //Estamos retornando um objeto com os mesmos atributos, mas com referências diferentes.

        //Esse método retornará o que for recebido como parâmetro do método.
        Mockito.when(categoryGateway.update(Mockito.any(Category.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        Assertions.assertTrue(initialCategory.isActive());
        Assertions.assertNull(initialCategory.getDeletedAt());

        //Act
        final var actualOutput = updateCategoryUseCase.execute(command).get();

        //Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        //Estamos garantindo que o "findById()", com o argumento "expectedId", foi chamado uma única vez.
        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        //Estamos garantindo que o "update()", com o argumento "category", foi chamado uma única vez.
        Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(
                updatedCategory -> //Estamos garantindo que o objeto passado para o método "update()" como argumento, que é o "updatedCategory" e, logo, é o objeto retornado por esse método, pois estamos utilizando o "returnsFirstArg()", é igual ao objeto que esperamos que seja retornado, que é o objeto com os valores atualizados.
                        Objects.equals(expectedId, updatedCategory.getId()) &&
                        Objects.equals(expectedName, updatedCategory.getName()) &&
                        Objects.equals(expectedDescription, updatedCategory.getDescription()) &&
                        Objects.equals(expectedIsActive, updatedCategory.isActive()) &&
                        (initialCategory.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())) &&
                        Objects.nonNull(updatedCategory.getDeletedAt())
        ));
    }

    //No teste abaixo, estamos simulando um erro genérico no gateway.
    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {

        //Arrange
        final var initialCategory = Category.newCategory("Film", null, true);

        final var expectedId = initialCategory.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.clone(initialCategory)));

        //Quando chamarmos o "update()", com o argumento "category", retornaremos um "DomainException".
        Mockito.when(categoryGateway.update(Mockito.any(Category.class))).thenThrow(new IllegalStateException(expectedErrorMessage));

        //Act
        final var notification = updateCategoryUseCase.execute(command).getLeft();

        //Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        //Estamos garantindo que o "update()", com o argumento "category", foi chamado uma única vez.
        Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(
                updatedCategory -> //Estamos garantindo que o objeto passado para o método "update()" como argumento, que é o "updatedCategory" e, logo, é o objeto retornado por esse método, pois estamos utilizando o "returnsFirstArg()", é igual ao objeto que esperamos que seja retornado, que é o objeto com os valores atualizados.
                        Objects.equals(expectedId, updatedCategory.getId()) &&
                        Objects.equals(expectedName, updatedCategory.getName()) &&
                        Objects.equals(expectedDescription, updatedCategory.getDescription()) &&
                        Objects.equals(expectedIsActive, updatedCategory.isActive()) &&
                        (initialCategory.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())) &&
                        Objects.isNull(updatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {

        //Arrange
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        Mockito.when(categoryGateway.findById(Mockito.eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        final var exception = Assertions.assertThrows(DomainException.class,
                () -> updateCategoryUseCase.execute(command));

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(CategoryID.from(expectedId)));

        Mockito.verify(categoryGateway, Mockito.never()).update(Mockito.any(Category.class));
    }
}
