package com.projetopraticobackend.servicocatalogo.application;

import com.projetopraticobackend.servicocatalogo.IntegrationTest;
import com.projetopraticobackend.servicocatalogo.application.category.create.CreateCategoryUseCase;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIT {

    @Autowired private CreateCategoryUseCase createCategoryUseCase;
    @Autowired private CategoryRepository categoryRepository;

    @Test
    public void testSpringInjections(){
        Assertions.assertNotNull(createCategoryUseCase);
        Assertions.assertNotNull(categoryRepository);
    }
}
