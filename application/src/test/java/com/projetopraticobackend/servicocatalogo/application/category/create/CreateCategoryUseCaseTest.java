package com.projetopraticobackend.servicocatalogo.application.category.create;

/* Nessa classe, teremos alguns testes do caso de uso da criação de uma categoria. */

import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Objects;

@ExtendWith(MockitoExtension.class) //Essa anotação serve para que o JUnit 5 inicialize tudo o que declararmos do Mockito, como o uso das anotações "@Mock" e "@InjectMocks".
@MockitoSettings(strictness = Strictness.WARN) //Essa anotação serve para que o Mockito nos avise quando estamos utilizando um mock que não está sendo utilizado, ao invés de, simplesmente, lançar um erro.
public class CreateCategoryUseCaseTest {

    @InjectMocks //Essa anotação serve para que o Mockito injete as dependências que estão sendo utilizadas no teste.
    private DefaultCreateCategoryUseCase useCase;

    @Mock //Essa anotação serve para que o Mockito crie um mock da interface que está sendo utilizada no teste.
    private CategoryGateway categoryGatewayMock;

    /* Abaixo, teremos os testes dos casos de uso de criação de uma categoria. */

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        /* O "command" é um sufixo para darmos uma semântica para o objeto que tem os
         * atributos de criação de uma categoria. Poderia ser um "DTO", por exemplo. */
        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        /* O Mockito criará, por baixo dos panos, a implementação da interface "CategoryGateway" com as
         * informações mockadas. */
        final CategoryGateway categoryGatewayMock = Mockito.mock(CategoryGateway.class);

        /* Acima, temos um mock burro, ou seja, sem comportamento. Para adicionarmos um comportamento para
         * o "mock", devemos utilizar o "when()" e o "then()". Ou seja, quando passarmos qualquer
         * valor para esse método, representado pelo "Mockito.any()", fazemos algo. */

        /* O "returnsFirstArg()", do "AdditionalAnswers", diz para o Mockito que, quando chamarmos o método
         * "create()" do "CategoryGateway", conforme especificado no "when()", devemos retornar o primeiro parâmetro
         * que for passado para o método "create()" do Gateway, que será a categoria nova. */

        /* O "thenAnswer" é utilizado porque não sabemos ainda qual será o retorno, ou seja, o retorno será dinâmico, de acordo com
        * o parâmetro passado para o método "create()". */
        Mockito.when(categoryGatewayMock.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var useCase = new DefaultCreateCategoryUseCase(categoryGatewayMock);

        /* Na literatura, os casos de uso implementam o padrão "Command". A ação que o comando
         * executa está na semântica da classe, como "CreateCategoryCommand", e ele tem apenas um
         * único método público, com o nome de "execute()". */
        final var actualOutput = useCase.execute(command).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        /* Abaixo, temos os asserts do Mockito. */

        /* Estamos garantindo que a implementação foi chamada uma única vez no método "create()", e recebendo como
        * parâmetro os métodos que estão sendo validados abaixo. */
        Mockito.verify(categoryGatewayMock, Mockito.times(1)).create(Mockito.argThat(
                /* Nessa lambda, compararemos o parâmetro de entrada com o parâmetro que esperamos que tenha sido
                 * enviado, ou seja, o objeto do tipo "Category" que foi passado para o "create()" com o objeto com os atributos que
                 * esperamos ter sido passado, que são representados pelo "expectedName" e etc. */
                category -> {
                    /* Assim, abaixo, esperamos que o "Category" que foi passado para o método
                    * "create()" do Gateway tenha sido uma categoria cujo atributo "name" é igual ao
                    * que está definido no "expectedName" e etc. */

                    return Objects.equals(expectedName, category.getName()) &&
                           Objects.equals(expectedDescription, category.getDescription()) &&
                           Objects.equals(expectedIsActive, category.isActive()) &&
                           Objects.nonNull(category.getId()) &&
                           Objects.nonNull(category.getCreatedAt()) &&
                           Objects.nonNull(category.getUpdatedAt()) &&
                           Objects.isNull(category.getDeletedAt());
                }
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException(){

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        /* O código abaixo está comentado pois, ao invés de utilizar o "Mockito.mock()", utilizamos o
        * "MockitoExtensions" para iniciarmos tudo o que declararmos do Mockito e as anotações "@Mock" e "@InjectMocks". */
//        final CategoryGateway categoryGatewayMock = Mockito.mock(CategoryGateway.class);

        Mockito.when(categoryGatewayMock.create(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        /* Estamos nos garantindo que o método "create" do Gateway não foi chamado, com
        * qualquer parâmetro, pois a "DomainException" deve ocorrer antes.*/
        Mockito.verify(categoryGatewayMock, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        /* O "command" é um sufixo para darmos uma semântica para o objeto que tem os
         * atributos de criação de uma categoria. Poderia ser um "DTO", por exemplo. */
        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        /* O Mockito criará, por baixo dos panos, a implementação da interface "CategoryGateway" com as
         * informações mockadas. */
        final CategoryGateway categoryGatewayMock = Mockito.mock(CategoryGateway.class);

        /* Acima, temos um mock burro, ou seja, sem comportamento. Para adicionarmos um comportamento para
         * o "mock", devemos utilizar o "when()" e o "then()". Ou seja, quando passarmos qualquer
         * valor para esse método, representado pelo "Mockito.any()", fazemos algo. */

        /* O "returnsFirstArg()", do "AdditionalAnswers", diz para o Mockito que, quando chamarmos o método
         * "create()" do "CategoryGateway", conforme especificado no "when()", devemos retornar o primeiro parâmetro
         * que for passado para o método "create()" do Gateway, que será a categoria nova. */

        /* O "thenAnswer" é utilizado porque não sabemos ainda qual será o retorno, ou seja, o retorno será dinâmico, de acordo com
         * o parâmetro passado para o método "create()". */
        Mockito.when(categoryGatewayMock.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var useCase = new DefaultCreateCategoryUseCase(categoryGatewayMock);

        /* Na literatura, os casos de uso implementam o padrão "Command". A ação que o comando
         * executa está na semântica da classe, como "CreateCategoryCommand", e ele tem apenas um
         * único método público, com o nome de "execute()". */
        final var actualOutput = useCase.execute(command).get(); //Como esse caso é um sucesso, estamos obtendo o "get()" do "Either".

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        /* Abaixo, temos os asserts do Mockito. */

        /* Estamos garantindo que a implementação foi chamada uma única vez no método "create()", e recebendo como
         * parâmetro os métodos que estão sendo validados abaixo. */
        Mockito.verify(categoryGatewayMock, Mockito.times(1)).create(Mockito.argThat(
                /* Nessa lambda, compararemos o parâmetro de entrada com o parâmetro que esperamos que tenha sido
                 * enviado, ou seja, o objeto do tipo "Category" que foi passado para o "create()" com o objeto com os atributos que
                 * esperamos ter sido passado, que são representados pelo "expectedName" e etc. */
                category -> {
                    /* Assim, abaixo, esperamos que o "Category" que foi passado para o método
                     * "create()" do Gateway tenha sido uma categoria cujo atributo "name" é igual ao
                     * que está definido no "expectedName" e etc. */

                    return Objects.equals(expectedName, category.getName()) &&
                           Objects.equals(expectedDescription, category.getDescription()) &&
                           Objects.equals(expectedIsActive, category.isActive()) &&
                           Objects.nonNull(category.getId()) &&
                           Objects.nonNull(category.getCreatedAt()) &&
                           Objects.nonNull(category.getUpdatedAt()) &&
                           Objects.nonNull(category.getDeletedAt());
                }
        ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        /* O "command" é um sufixo para darmos uma semântica para o objeto que tem os
         * atributos de criação de uma categoria. Poderia ser um "DTO", por exemplo. */
        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        /* O Mockito criará, por baixo dos panos, a implementação da interface "CategoryGateway" com as
         * informações mockadas. */
        final CategoryGateway categoryGatewayMock = Mockito.mock(CategoryGateway.class);

        /* Acima, temos um mock burro, ou seja, sem comportamento. Para adicionarmos um comportamento para
         * o "mock", devemos utilizar o "when()" e o "then()". Ou seja, quando passarmos qualquer
         * valor para esse método, representado pelo "Mockito.any()", fazemos algo. */

        /* Estamos fazendo com que o gateway retorne uma exception genérica */
        Mockito.when(categoryGatewayMock.create(Mockito.any())).thenThrow(new IllegalStateException("Gateway error"));

        final var useCase = new DefaultCreateCategoryUseCase(categoryGatewayMock);

        /* Na literatura, os casos de uso implementam o padrão "Command". A ação que o comando
         * executa está na semântica da classe, como "CreateCategoryCommand", e ele tem apenas um
         * único método público, com o nome de "execute()". */

        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        /* Estamos garantindo que, apesar da exception ter sido lançada, o método "create()" foi chamado, pois a
        * exception foi lançada apenas no gateway, e garantimos que o objeto que passamos para o Gateway está
        * cumprindo todas as validações abaixo. */
        Mockito.verify(categoryGatewayMock, Mockito.times(1)).create(Mockito.argThat(
                category -> {

                    return Objects.equals(expectedName, category.getName()) &&
                           Objects.equals(expectedDescription, category.getDescription()) &&
                           Objects.equals(expectedIsActive, category.isActive()) &&
                           Objects.nonNull(category.getId()) &&
                           Objects.nonNull(category.getCreatedAt()) &&
                           Objects.nonNull(category.getUpdatedAt()) &&
                           Objects.nonNull(category.getDeletedAt());
                }
        ));
    }
}