package com.projetopraticobackend.servicocatalogo.infrastructure.category;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;
import com.projetopraticobackend.servicocatalogo.domain.category.CategorySearchQuery;
import com.projetopraticobackend.servicocatalogo.MySQLGatewayTest;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    @BeforeEach
    void cleanUp(){
        categoryRepository.deleteAll();
    }

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

        //Estamos garantindo que não temos nada no banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());

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

        Assertions.assertNull(categoryFromRepository.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated(){

        //Esses são os valores que a categoria, após a atualização, deve ter.
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes de todos os gêneros.";
        final var expectedIsActive = true;

        //A categoria abaixo está errada e será atualizada.
        final var invalidCategory = Category.newCategory("Film", null, true);

        //Estamos garantindo que não temos nada no banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());

        //Estamos persistindo a categoria com os valores inválidos.
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(invalidCategory));

        //Estamos garantindo que a categoria foi persistida.
        Assertions.assertEquals(1, categoryRepository.count());

        //Estamos garantindo que a categoria, que foi criada com os valores inválidos, foi persistida com os valores inválidos.
        final var invalidCategoryFromRepository = categoryRepository.findById(invalidCategory.getId().getValue()).get();
        Assertions.assertEquals("Film", invalidCategoryFromRepository.getName());
        Assertions.assertEquals(null, invalidCategoryFromRepository.getDescription());
        Assertions.assertEquals(true, invalidCategoryFromRepository.isActive());

        //Estamos criando uma nova instância com os valores que são esperados.
        final var updatedCategory =
                invalidCategory.clone().update(expectedName, expectedDescription, expectedIsActive);

        //Estamos atualizando a categoria.
        final var savedUpdatedCategory = categoryMySQLGateway.update(updatedCategory);

        //Estamos nos certificando que, após a atualização, ainda temos apenas uma categoria no banco de dados.
        Assertions.assertEquals(1, categoryRepository.count());

        //Abaixo, temos as asserções para verificar o que foi retornado do gateway.

        Assertions.assertEquals(invalidCategory.getId(), savedUpdatedCategory.getId());
        Assertions.assertEquals(expectedName, savedUpdatedCategory.getName());
        Assertions.assertEquals(expectedDescription, savedUpdatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, savedUpdatedCategory.isActive());

        Assertions.assertEquals(invalidCategory.getCreatedAt(), savedUpdatedCategory.getCreatedAt());
        Assertions.assertTrue(invalidCategory.getUpdatedAt().isBefore(savedUpdatedCategory.getUpdatedAt()));
        Assertions.assertEquals(invalidCategory.getDeletedAt(), savedUpdatedCategory.getDeletedAt());

        Assertions.assertNull(savedUpdatedCategory.getDeletedAt());

        //Abaixo, temos as asserções para a categoria que foi, de fato, persistida no repositório, sem utilizarmos
        //o retorno do gateway.

        final var categoryFromRepository = categoryRepository.findById(invalidCategory.getId().getValue()).get();

        Assertions.assertEquals(invalidCategory.getId().getValue(), categoryFromRepository.getId());
        Assertions.assertEquals(expectedName, categoryFromRepository.getName());
        Assertions.assertEquals(expectedDescription, categoryFromRepository.getDescription());
        Assertions.assertEquals(expectedIsActive, categoryFromRepository.isActive());

        Assertions.assertEquals(invalidCategory.getCreatedAt(), categoryFromRepository.getCreatedAt());
        Assertions.assertTrue(invalidCategory.getUpdatedAt().isBefore(categoryFromRepository.getUpdatedAt()));
        Assertions.assertEquals(invalidCategory.getDeletedAt(), categoryFromRepository.getDeletedAt());

        Assertions.assertNull(categoryFromRepository.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory(){

        final var category = Category.newCategory("Filmes", null, true);

        //Estamos garantindo que não temos nada no banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());

        //Estamos persistindo a categoria que será deletada no banco de dados..
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));

        //Estamos garantindo que a categoria foi persistida.
        Assertions.assertEquals(1, categoryRepository.count());

        categoryMySQLGateway.deleteById(category.getId());

        //Estamos garantindo que a categoria foi deletada corretamente do banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAnInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory(){

        Assertions.assertEquals(0, categoryRepository.count()); //Estamos garantindo que não temos nada no banco de dados.

        categoryMySQLGateway.deleteById(CategoryID.from("123"));

        //Estamos garantindo que nenhuma categoria foi deletada, pois não existe nenhuma categoria como essa no banco de dados.
        //De acordo com a nossa implementação, se o ID não existir, não retornaremos um erro, por isso não verificaremos a
        //exception.
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAValidPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory(){

        //Esses são os valores que a categoria, após a atualização, deve ter.
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes de todos os gêneros.";
        final var expectedIsActive = true;

        //A categoria abaixo será salva no banco de dados
        final var validCategory = Category.newCategory("Filmes", "Filmes de todos os gêneros.", true);

        //Estamos garantindo que não temos nada no banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());

        //Estamos persistindo a categoria com os valores válidos.
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(validCategory));

        //Estamos garantindo que a categoria foi persistida.
        Assertions.assertEquals(1, categoryRepository.count());

        //Estamos garantindo que a categoria, que foi criada com os valores válidos, foi persistida com os valores válidos.
        final var savedCategory = categoryMySQLGateway.findById(validCategory.getId()).get();

        Assertions.assertEquals(validCategory.getId(), savedCategory.getId());
        Assertions.assertEquals(expectedName, savedCategory.getName());
        Assertions.assertEquals(expectedDescription, savedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, savedCategory.isActive());

        Assertions.assertEquals(validCategory.getCreatedAt(), savedCategory.getCreatedAt());
        Assertions.assertEquals(validCategory.getUpdatedAt(), savedCategory.getUpdatedAt());
        Assertions.assertNull(savedCategory.getDeletedAt());

        //Estamos nos certificando que apenas uma categoria foi buscada.
        Assertions.assertEquals(1, categoryRepository.count());
    }

    @Test
    public void givenAValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty(){

        //Estamos garantindo que não temos nada no banco de dados.
        Assertions.assertEquals(0, categoryRepository.count());

        //Estamos garantindo que a categoria, que foi criada com os valores válidos, foi persistida com os valores válidos.
        final var category = categoryMySQLGateway.findById(CategoryID.from("123"));

        //Estamos garantindo que a categoria não foi encontrada.
        Assertions.assertTrue(category.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated(){

        final var expectedPage = 0; //É a página que queremos buscar.
        final var expectedPerPage = 1; //Queremos um resultado por página.
        final var expectedTotal = 3; //Criaremos três categories pré-persistidas.

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);

        Assertions.assertEquals(0, categoryRepository.count()); //Estamos garantindo que não existe nada persistido previamente.

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count()); //Estamos garantindo que as três categorias foram persistidas.

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc"); //Buscaremos as categorias com esses parâmetros.

        final var categories = categoryMySQLGateway.findAll(query); //Estamos buscando a categoria.

        //Estamos realizando as asserções para garantir que a paginação está funcionando corretamente.
        Assertions.assertEquals(expectedPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(expectedTotal, categories.totalElements());
        Assertions.assertEquals(expectedPerPage, categories.elements().size());

        //Como os parâmetros de busca estão com a ordenação por nome em ordem crescente, a primeira categoria deve ser "Documentários".
        Assertions.assertEquals(documentarios.getId(), categories.elements().get(0).getId());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage(){

        //Nesse teste, a paginação não trará nenhum resultado.

        final var expectedPage = 0; //É a página que queremos buscar.
        final var expectedPerPage = 1; //Queremos um resultado por página.
        final var expectedTotal = 0; //Criaremos três categories pré-persistidas.

        Assertions.assertEquals(0, categoryRepository.count()); //Estamos garantindo que não existe nada persistido previamente.

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc"); //Buscaremos as categorias com esses parâmetros.

        final var categories = categoryMySQLGateway.findAll(query); //Estamos buscando a categoria.

        //Estamos realizando as asserções para garantir que a paginação está funcionando corretamente.
        Assertions.assertEquals(expectedPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(expectedTotal, categories.totalElements());
        Assertions.assertEquals(0, categories.elements().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated(){

        //O teste abaixo fará o seguimento da paginação.

        var expectedPage = 0; //É a página que queremos buscar.
        final var expectedPerPage = 1; //Queremos um resultado por página.
        final var expectedTotal = 3; //Criaremos três categories pré-persistidas.

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);

        Assertions.assertEquals(0, categoryRepository.count()); //Estamos garantindo que não existe nada persistido previamente.

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count()); //Estamos garantindo que as três categorias foram persistidas.

        var query = new CategorySearchQuery(0, 1, "", "name", "asc"); //Buscaremos as categorias com esses parâmetros.

        var categories = categoryMySQLGateway.findAll(query); //Estamos buscando as categorias.

        //Asserções da realização da primeira paginação. - Deve retornar a categoria "Documentários".

        //Estamos realizando as asserções para garantir que a paginação está funcionando corretamente.
        Assertions.assertEquals(expectedPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(expectedTotal, categories.totalElements());
        Assertions.assertEquals(expectedPerPage, categories.elements().size());

        //Como os parâmetros de busca estão com a ordenação por nome em ordem crescente, a primeira categoria deve ser "Documentários".
        Assertions.assertEquals(documentarios.getId(), categories.elements().get(0).getId());

        //Testes da página 01 - Deve retornar a categoria "Filmes".

        expectedPage = 1;
        query = new CategorySearchQuery(1, 1, "", "name", "asc"); //Buscaremos as categorias com esses parâmetros.
        categories = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(expectedTotal, categories.totalElements());
        Assertions.assertEquals(expectedPerPage, categories.elements().size());

        Assertions.assertEquals(filmes.getId(), categories.elements().get(0).getId());

        //Testes da página 02 - Deve buscar a categoria "Séries".

        expectedPage = 2;
        query = new CategorySearchQuery(2, 1, "", "name", "asc"); //Buscaremos as categorias com esses parâmetros.
        categories = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(expectedTotal, categories.totalElements());
        Assertions.assertEquals(expectedPerPage, categories.elements().size());

        Assertions.assertEquals(series.getId(), categories.elements().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchesCategoryName_shouldReturnPaginated(){

        //Abaixo, testaremos o uso dos termos na paginação no nome da categoria.

        final var expectedPage = 0; //É a página que queremos buscar.
        final var expectedPerPage = 1; //Queremos um resultado por página.
        final var expectedTotal = 1; //Criaremos três categories pré-persistidas.

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);

        Assertions.assertEquals(0, categoryRepository.count()); //Estamos garantindo que não existe nada persistido previamente.

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count()); //Estamos garantindo que as três categorias foram persistidas.

        var query = new CategorySearchQuery(0, 1, "doc", "name", "asc"); //Buscaremos as categorias com esses parâmetros.

        var categories = categoryMySQLGateway.findAll(query); //Estamos buscando as categorias.

        //Asserções da realização da primeira paginação. - Deve retornar a categoria "Documentários".

        //Estamos realizando as asserções para garantir que a paginação está funcionando corretamente.
        Assertions.assertEquals(expectedPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(expectedTotal, categories.totalElements());
        Assertions.assertEquals(expectedPerPage, categories.elements().size());

        //Como os parâmetros de busca estão com a ordenação por nome em ordem crescente, a primeira categoria deve ser "Documentários".
        Assertions.assertEquals(documentarios.getId(), categories.elements().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchesCategoryDescription_shouldReturnPaginated(){

        //Abaixo, testaremos o uso dos termos na paginação no nome da categoria.

        final var expectedPage = 0; //É a página que queremos buscar.
        final var expectedPerPage = 1; //Queremos um resultado por página.
        final var expectedTotal = 1; //Criaremos três categories pré-persistidas.

        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var series = Category.newCategory("Séries", "Uma categoria assistida", true);
        final var documentarios = Category.newCategory("Documentários", "A categoria menos assistida", true);

        Assertions.assertEquals(0, categoryRepository.count()); //Estamos garantindo que não existe nada persistido previamente.

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count()); //Estamos garantindo que as três categorias foram persistidas.

        //Abaixo, estamos buscando as categorias com a descrição "Mais assistida". Deverá retornar apenas a categoria "Filmes".
        var query = new CategorySearchQuery(0, 1, "Mais assistida", "name", "asc"); //Buscaremos as categorias com esses parâmetros.

        var categories = categoryMySQLGateway.findAll(query); //Estamos buscando as categorias.

        //Estamos realizando as asserções para garantir que a paginação está funcionando corretamente.
        Assertions.assertEquals(expectedPage, categories.currentPage());
        Assertions.assertEquals(expectedPerPage, categories.perPage());
        Assertions.assertEquals(expectedTotal, categories.totalElements());
        Assertions.assertEquals(expectedPerPage, categories.elements().size());

        //Como os parâmetros de busca estão com a ordenação por descrição, deverá retornar a categoria "filmes".
        Assertions.assertEquals(filmes.getId(), categories.elements().get(0).getId());
    }
}