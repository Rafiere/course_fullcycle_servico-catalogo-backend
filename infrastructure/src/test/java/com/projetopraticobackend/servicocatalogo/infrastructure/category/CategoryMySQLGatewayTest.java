package com.projetopraticobackend.servicocatalogo.infrastructure.category;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.infrastructure.MySQLGatewayTest;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/* O gateway será injetado. Para testarmos o gateway, precisaremos do repositório, pois, com
* o repositório, conseguimos manipular as informações do banco de dados sem precisarmos passar pelo
* gateway sempre. */

/* Existem algumas formas de testar a implementação do gateway. Algumas pessoas testam como
* se fosse um teste unitário, mockando tudo o que entra e sai do repositório. A segunda forma, que é a
* que o professor recomenda, é testar o repositório e o gateway de maneira integrada, pois, na prática, não
* vamos usar o repositório sozinho. Sempre utilizaremos o repositório através de gateways. */

//Abaixo, temos uma abstração, que foi criada para não termos que repetir as anotações acima, que prepararão o setup do teste.
@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    @Autowired private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired private CategoryRepository categoryRepository;

// Sempre queremos deletar todas as informações que foram manipuladas pelo teste anterior ao utilizarmos um teste integrado.
// Para isso, podemos utilizar o método abaixo ou criarmos uma "extension".
//    @BeforeEach
//    void cleanUp(){
//        categoryRepository.deleteAll();
//    }

    /* Esse teste garantirá que as dependências foram injetadas corretamente. */
    @Test
    public void givenValidDependencies_whenInjects_shouldReturnInstances(){
        assertNotNull(categoryMySQLGateway);
        assertNotNull(categoryRepository);
    }

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes de todos os gêneros.";
        final var expectedActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);

        Assertions.assertEquals(0, categoryRepository.count()); //Estamos garantindo que não temos nada no banco de dados.

        final var returnedFromGatewayCategory = categoryMySQLGateway.create(category);

        Assertions.assertEquals(1, categoryRepository.count());

        //Abaixo, temos as asserções para verificar o que foi retornado do gateway.

        Assertions.assertEquals(category.getId(), returnedFromGatewayCategory.getId());
        Assertions.assertEquals(expectedName, returnedFromGatewayCategory.getName());
        Assertions.assertEquals(expectedDescription, returnedFromGatewayCategory.getDescription());
        Assertions.assertEquals(expectedActive, returnedFromGatewayCategory.isActive());

        Assertions.assertEquals(category.getCreatedAt(), returnedFromGatewayCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), returnedFromGatewayCategory.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), returnedFromGatewayCategory.getDeletedAt());

        Assertions.assertNull(returnedFromGatewayCategory.getDeletedAt());

        //Abaixo, temos as asserções para a categoria que foi, de fato, persistida no repositório, sem utilizarmos
        //o retorno do gateway.

        final var categoryFromRepository = categoryRepository.findById(category.getId().getValue()).get();

        Assertions.assertEquals(category.getId().getValue(), categoryFromRepository.getId());
        Assertions.assertEquals(expectedName, categoryFromRepository.getName());
        Assertions.assertEquals(expectedDescription, categoryFromRepository.getDescription());
        Assertions.assertEquals(expectedActive, categoryFromRepository.isActive());

        Assertions.assertEquals(category.getCreatedAt(), categoryFromRepository.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), categoryFromRepository.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), categoryFromRepository.getDeletedAt());

        Assertions.assertNull(returnedFromGatewayCategory.getDeletedAt());
    }
}