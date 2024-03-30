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
import com.projetopraticobackend.servicocatalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.Optional;

@Service
//Algumas pessoas usam o "@Service" pois essa é uma classe de serviço da camada de infraestrutura, outras preferem utilizar o "@Component".
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository; //Precisamos do "Repository" para nos comunicarmos com o Hibernate e acessarmos a camada de persistência.

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //Criaremos testes para cada um dos métodos abaixo.

    @Override
    public Category create(final Category category) {
        return save(category);
    }

    @Override
    public void deleteById(final CategoryID id) {

        //O professor utilizou o "exists" antes para não lidar com exceptions.
        if (categoryRepository.existsById(id.getValue())) {
            categoryRepository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID id) {
        return categoryRepository.findById(id.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category category) {
        return save(category);
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery categorySearchQuery) {
        //A paginação, no Spring, é feita através do "Specification", que é uma abstração
        //feita em cima da "Criteria API" do JPA.

        final var page = PageRequest.of( //Esse objeto define as configurações da paginação que será utilizada.
                categorySearchQuery.page(),
                categorySearchQuery.perPage(),
                Sort.by(Sort.Direction.fromString(categorySearchQuery.direction()), categorySearchQuery.sort()));

        //Busca dinâmica com Specification.
        //Vamos verificar se existe o parâmetro "terms". Se existir, vamos aplicar a busca.

        final var specifications = Optional.ofNullable(categorySearchQuery.terms())
                .filter(terms -> !terms.isBlank()) //O "filter" é para apenas passarmos essa "Specification" se algum termo tiver sido enviado.
                .map(term -> {
                    //Abaixo, temos o "like" apenas para o atributo "name" e "description".

                    //Temos que implementar a lambda abaixo para passarmos o "Specification" com os parâmetros da consulta.
                    //O "root" é usado para pegarmos uma propriedade dentro do objeto.

                    //Estamos construindo o parâmetro "LIKE" do SQL com o atributo "name" ou "descrpition" sendo convertido para "UPPERCASE".
                    final Specification<CategoryJpaEntity> nameLike = SpecificationUtils.like("name", term);

                    final Specification<CategoryJpaEntity> descriptionLike = SpecificationUtils.like("description", term);

                    return nameLike.or(descriptionLike); //Estamos fazendo um "OR" entre os dois "Specifications" (nameLike e descriptionLike
                })
                .orElse(null);

        final var pageResult = this.categoryRepository.findAll(Specification.where(specifications), page);

        //Estamos convertendo para o nosso objeto "Pagination" e retornando-o com os resultados.
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).stream().toList());
    }

    //Como tanto o método "create" quanto o "update" fazem a mesma coisa, criamos um método privado para evitar a repetição de código.
    //Esse método interno faz a conversão de um agregado para uma entidade JPA e salva essa entidade no banco de dados.
    private Category save(final Category category) {
        final CategoryJpaEntity categoryJpaEntity = CategoryJpaEntity.from(category);

        final CategoryJpaEntity savedCategoryJpaEntity = categoryRepository.save(categoryJpaEntity);

        return CategoryJpaEntity.toAggregate(savedCategoryJpaEntity);
    }
}