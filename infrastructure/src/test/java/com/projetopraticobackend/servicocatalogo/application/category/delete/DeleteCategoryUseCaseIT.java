package com.projetopraticobackend.servicocatalogo.application.category.delete;

import com.projetopraticobackend.servicocatalogo.IntegrationTest;
import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
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
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK(){

        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var expectedId = category.getId();

        save(category);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCategory_shouldBeOK(){

        final var expectedId = "123";

        Assertions.assertEquals(0, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var expectedId = category.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    private void save(final Category... categories){

        final List<CategoryJpaEntity> categoriesEntities = Arrays.stream(categories)
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categoriesEntities);
    }
}