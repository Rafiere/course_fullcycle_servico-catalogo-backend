package com.projetopraticobackend.servicocatalogo.infrastructure.category;

/* Essa é a implementação do Gateway de "Category" para o MySQL. */

/* O "application" usa o Gateway para se comunicar com toda a camada de persistência, dessa
* forma, essa é a implementação desse Gateway. */

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryGateway;
import com.projetopraticobackend.servicocatalogo.domain.category.CategoryID;
import com.projetopraticobackend.servicocatalogo.domain.category.CategorySearchQuery;
import com.projetopraticobackend.servicocatalogo.domain.pagination.Pagination;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //Algumas pessoas usam o "@Service" pois essa é uma classe de serviço da camada de infraestrutura, outras preferem utilizar o "@Component".
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository; //Precisamos do "Repository" para nos comunicarmos com o Hibernate e acessarmos a camada de persistência.

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //Criaremos testes para cada um dos métodos abaixo.

    @Override
    public Category create(Category category) {
        final CategoryJpaEntity categoryJpaEntity = CategoryJpaEntity.from(category);

        final CategoryJpaEntity savedCategoryJpaEntity = categoryRepository.save(categoryJpaEntity);

        return CategoryJpaEntity.toAggregate(savedCategoryJpaEntity);
    }

    @Override
    public void deleteById(final CategoryID id) {

    }

    @Override
    public Optional<Category> findById(final CategoryID id) {
        return Optional.empty();
    }

    @Override
    public Category update(final Category category) {
        return null;
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery query) {
        return null;
    }
}