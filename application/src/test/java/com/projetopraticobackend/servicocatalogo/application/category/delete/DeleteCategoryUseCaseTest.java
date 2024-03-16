    package com.projetopraticobackend.servicocatalogo.application.category.delete;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase deleteCategoryUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK(){

        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var expectedId = category.getId();

        //Para mockarmos um método que não retorna nada, usamos o "doNothing".
        //Assim, invertemos a ordem. Colocamos o que será feito e, após isso, colocamos quando será feito.
        Mockito.doNothing().when(categoryGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    //Mesmo se o ID não existir, não retornaremos um erro.
    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK(){

        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var expectedId = CategoryID.from("123");

        //Para mockarmos um método que não retorna nada, usamos o "doNothing".
        //Assim, invertemos a ordem. Colocamos o que será feito e, após isso, colocamos quando será feito.
        Mockito.doNothing().when(categoryGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){

        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var expectedId = category.getId();

        //Para mockarmos um método que não retorna nada, usamos o "doNothing".
        //Assim, invertemos a ordem. Colocamos o que será feito e, após isso, colocamos quando será feito.
        Mockito.doThrow(new IllegalStateException(("Gateway error"))).when(categoryGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class,
                () -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }
}