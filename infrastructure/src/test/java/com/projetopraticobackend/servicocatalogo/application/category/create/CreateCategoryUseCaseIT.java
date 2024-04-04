package com.projetopraticobackend.servicocatalogo.application.category.create;

import com.projetopraticobackend.servicocatalogo.IntegrationTest;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    //Como já criamos testes para o caso de uso, bastava apenas criarmos
    //um teste simples de integração com o caminho feliz, porém, como estamos
    //em um curso, vamos criar vários testes de integração.

    @Autowired //Estamos fazendo um teste de integração. Não utilizaremos "mocks".
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean //Estamos misturando o Mockito com o Spring. Com isso, o Mockito pegará o "bean" do Spring e colocará uma capa nele, que é a capa do
             //próprio Mockito, como se fosse uma capa de espião, e ficará vendo todas as chamadas que são feitas para ele.

             //Assim, quando usamos um "spy", o Mockito não cria um objeto novo, ele pega um objeto que já existe e coloca uma capa nele. Dessa forma, a
             //implementação do "bean" é mantida, ou seja, continuamos chamar o método real, mas o Mockito fica vendo tudo o que acontece com ele.
    private CategoryGateway categoryGatewaySpy;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        //Não deve existir nenhuma categoria persistida no banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());

        /* O "command" é um sufixo para darmos uma semântica para o objeto que tem os
         * atributos de criação de uma categoria. Poderia ser um "DTO", por exemplo. */
        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var createdCategory = createCategoryUseCase.execute(command).get();

        Assertions.assertNotNull(createdCategory);
        Assertions.assertNotNull(createdCategory.id());

        //Deve existir uma categoria persistida no banco de dados após o retorno do "output".
        Assertions.assertEquals(1, categoryRepository.count());

        //Na vida real, não devemos utilizar o "get()" quando utilizamos o Optional.
        //O Brian Goetz, que é o criador da especificação do Optional, diz que se arrepende de
        //ter colocado esse método.
        final var category = categoryRepository.findById(createdCategory.id().getValue()).get();

        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());

        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException(){

        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        //Não deve ter nenhuma categoria persistida no banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());

        final var notification = createCategoryUseCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        //Não deve ter nenhuma categoria persistida no banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());

        //Com isso, como estamos utilizando um "Spy", conseguimos verificar se o método
        //"create" foi chamado ou não, sem criar um mock dele, ou seja, usando a
        //implementação real.

        //Assim, não utilizamos o Mockito apenas em testes unitários, mas também em testes de integração.
        Mockito.verify(categoryGatewaySpy, Mockito.never()).create(Mockito.any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId(){

            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedIsActive = false;

            //Não deve existir nenhuma categoria persistida no banco de dados.
            Assertions.assertEquals(0, categoryRepository.count());

            final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

            final var createdCategory = createCategoryUseCase.execute(command).get();

            Assertions.assertNotNull(createdCategory);
            Assertions.assertNotNull(createdCategory.id());

            //Deve existir uma categoria persistida no banco de dados após o retorno do "output".
            Assertions.assertEquals(1, categoryRepository.count());

            //Na vida real, não devemos utilizar o "get()" quando utilizamos o Optional.
            //O Brian Goetz, que é o criador da especificação do Optional, diz que se arrepende de
            //ter colocado esse método.
            final var category = categoryRepository.findById(createdCategory.id().getValue()).get();

            Assertions.assertEquals(expectedName, category.getName());
            Assertions.assertEquals(expectedDescription, category.getDescription());
            Assertions.assertEquals(expectedIsActive, category.isActive());

            Assertions.assertNotNull(category.getCreatedAt());
            Assertions.assertNotNull(category.getUpdatedAt());
            Assertions.assertNotNull(category.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        //Não deve ter nenhuma categoria persistida no banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());

        //Devemos nos certificar que uma exception será lançada.
        //Como o "categoryGateway" está com o "@SpyBean", ele chamará a implementação real, e não
        //um "mock" do Mockito, assim, se o erro ocorrer, ele será real.

        //O "doThrow" deve ser utilizado quando utilizamos o "@Spy", ou seja, quando existe, de fato, uma implementação real. O "doThrow()", o "doAnswer()" e o "doReturn()".
        Mockito.doThrow(new RuntimeException("Gateway error")).when(categoryGatewaySpy).create(Mockito.any());

        //O Mockito com o "Mockito.when()" deve ser utilizado quando utilizamos o "@Mock" ou o "@MockBean".
//        Mockito.when(categoryGatewaySpy.create(Mockito.any())).thenThrow(new IllegalStateException("Gateway error"));

        final var actualException = createCategoryUseCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


        //Não deve ter nenhuma categoria persistida no banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());
    }
}