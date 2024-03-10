package com.projetopraticobackend.servicocatalogo.application.category.update;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
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
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){

        //Arrange

        final var initialCategory = Category.newCategory("Film", null, true);

        final var expectedId = initialCategory.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        //Precisamos mockar o comportamento do teste.

        //Teremos duas chamadas para o gateway. O "find" para buscar a categoria e o "update" para atualizar a categoria na camada de persistência.

        //Quando chamarmos o "findById" chamando o "expectedId", retornaremos um "Optional" com a categoria que foi criada inicialmente.
        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(initialCategory));

        //Esse método retornará o que for recebido como parâmetro do método.
        Mockito.when(categoryGateway.update(Mockito.any(Category.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //Act
        final var actualOutput = useCase.execute(command).get();

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
}
