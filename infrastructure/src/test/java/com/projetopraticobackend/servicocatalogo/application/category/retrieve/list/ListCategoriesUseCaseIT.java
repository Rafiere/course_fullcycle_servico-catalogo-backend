package com.projetopraticobackend.servicocatalogo.application.category.retrieve.list;

import com.projetopraticobackend.servicocatalogo.IntegrationTest;
import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategorySearchQuery;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IntegrationTest
public class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase listCategoriesUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    /* O método "mockUp()" abaixo servirá para criarmos todos os "mocks" no banco de dados. Eles serão
     * utilizados para todos os métodos de teste existentes. */
    @BeforeEach
    void mockUp() {

        /* A cada teste, essas categorias serão salvas no banco de dados. Não precisaremos limpar o
         * banco de dados pois a anotação "@IntegrationTest" já faz isso através de uma extensão do
         * Mockito que criamos. */

        final var categories = Stream.of(
                        Category.newCategory("Filmes", null, true),
                        Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix", true),
                        Category.newCategory("Amazon Originals", "Tìtulos de autoria da Amazon Prime", true),
                        Category.newCategory("Documentários", null, true),
                        Category.newCategory("Sports", null, true),
                        Category.newCategory("Kids", "Categoria para crianças", true),
                        Category.newCategory("Series", null, true)
                )
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    /* Esse teste verifica que, se passarmos um termo que não existe no banco de dados, uma lista
     * vazia será retornada ao invés de uma exception. */
    @Test
    public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() {

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "abcdefghij";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var query =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var queryResult = listCategoriesUseCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, queryResult.elements().size());
        Assertions.assertEquals(expectedPage, queryResult.currentPage());
        Assertions.assertEquals(expectedPerPage, queryResult.perPage());
        Assertions.assertEquals(expectedTotal, queryResult.totalElements());
    }

    /* Queremos testar que, dado um termo de busca válido, as categorias filtradas deverão ser retornadas. */

    /* Para isso, utilizaremos um teste parametrizado, ou seja, um teste que testa um mesmo cenário com
     * diferentes parâmetros, assim, na primeira execução, os valores da primeira linha serão utilizados, na segunda
     * execução, os valores da segunda linha serão utilizados e etc. */

    @ParameterizedTest
    //Essa anotação é utilizada para indicar que o método abaixo é um teste parametrizado. Ele pode ser utilizado para testar um mesmo cenário com diferentes parâmetros.
    @CsvSource({ //Essa anotação é utilizada para passarmos parâmetros para o teste no formado ".csv", assim, cada um dos valores abaixo, separados por vírgula, serão passados como parâmetros para o teste.
            "fil,0,10,1,1,Filmes", //Se passarmos o termo "fil", com o "expectedPage" sendo "0", o "expectedPerPage" sendo 10 e etc, o nome da categoria retornada deverá ser "Filmes".
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "KI,0,10,1,1,Kids",
            "crianças,0,10,1,1,Kids",
            "da Amazon,0,10,1,1,Amazon Originals"
    })
    public void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var queryResult = listCategoriesUseCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, queryResult.elements().size());
        Assertions.assertEquals(expectedPage, queryResult.currentPage());
        Assertions.assertEquals(expectedPerPage, queryResult.perPage());
        Assertions.assertEquals(expectedTotal, queryResult.totalElements());
        Assertions.assertEquals(expectedCategoryName, queryResult.elements().get(0).name());
    }

    /* Nesse teste abaixo, testaremos a ordenação. */

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals", //Com isso, ordenaremos pelo nome na direção ascendente. Queremos ter sete itens na primeira página e a primeira categoria deverá ser o "Amazon Originals".
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Filmes",
            "createdAt,desc,0,10,7,7,Series"
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {

        final var expectedTerms = ""; //Nesse teste, não queremos filtrar por nada.

        final var query =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var queryResult = listCategoriesUseCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, queryResult.elements().size());
        Assertions.assertEquals(expectedPage, queryResult.currentPage());
        Assertions.assertEquals(expectedPerPage, queryResult.perPage());
        Assertions.assertEquals(expectedTotal, queryResult.totalElements());
        Assertions.assertEquals(expectedCategoryName, queryResult.elements().get(0).name());
    }

    /* O teste abaixo fará o teste da paginação. */

    @ParameterizedTest //Existem várias maneiras de passarmos o "source", não apenas o "@CsvSource".
    @CsvSource({ //Abaixo, estamos testando as páginas "0", "1" e "2" com "2" itens por página, exceto na última, que temos apenas "1" pois acabaram os valores.
            "0,2,2,7,Amazon Originals;Documentários", //O ";" fará um "split" do último valor para fazermos o "match" dos dois valores.
            "1,2,2,7,Filmes;Kids",
            "2,2,2,7,Netflix Originals;Series",
            "3,2,1,7,Sports"
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesNames
    ) {

        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var queryResult = listCategoriesUseCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, queryResult.elements().size());
        Assertions.assertEquals(expectedPage, queryResult.currentPage());
        Assertions.assertEquals(expectedPerPage, queryResult.perPage());
        Assertions.assertEquals(expectedTotal, queryResult.totalElements());

        int index = 0;
        for(String expectedName: expectedCategoriesNames.split(";")) {

            final String actualName = queryResult.elements().get(index).name();

            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }
}
