package com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence;

import com.projetopraticobackend.servicocatalogo.domain.category.Category;
import com.projetopraticobackend.servicocatalogo.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired private CategoryRepository categoryRepository;

    /* O teste abaixo validará se um erro será enviado quando o nome for nulo, já que isso não é recomendado. */
    @Test
    public void givenAnInvalidNullName_whenCallsSave_shouldReturnError(){

        final var category = Category.newCategory("Filmes", "Filmes de todos os gêneros.", true);

        final var entity = CategoryJpaEntity.from(category);
        entity.setName(null);

        //Se passarmos um nome nulo, o Hibernate lançará uma exceção, pois o nome é uma propriedade que não pode ser nula.

        //O "DataIntegrityViolationException" é só uma exception genérica que o Spring usa para encapsular algum problema.
        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(entity));

        //A "PropertyValueException" é a exception que o Hibernate usa para validar as propriedades de uma entidade. É ela a exception
        //raiz, que é encapsulada pela "DataIntegrityViolationException", então estamos verificando se a causa da "DataIntegrityViolationException" foi
        //a "PropertyValueException".

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals("name", actualCause.getPropertyName());
        Assertions.assertEquals("not-null property references a null or transient value : com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryJpaEntity.name", actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError(){

        final var category = Category.newCategory("Filmes", "Filmes de todos os gêneros.", true);

        final var entity = CategoryJpaEntity.from(category);
        entity.setCreatedAt(null);

        //Se passarmos o "createdAt" nulo, o Hibernate lançará uma exceção, pois o nome é uma propriedade que não pode ser nula.

        //O "DataIntegrityViolationException" é só uma exception genérica que o Spring usa para encapsular algum problema.
        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(entity));

        //A "PropertyValueException" é a exception que o Hibernate usa para validar as propriedades de uma entidade. É ela a exception
        //raiz, que é encapsulada pela "DataIntegrityViolationException", então estamos verificando se a causa da "DataIntegrityViolationException" foi
        //a "PropertyValueException".

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals("createdAt", actualCause.getPropertyName());
        Assertions.assertEquals("not-null property references a null or transient value : com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryJpaEntity.createdAt", actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError(){

        final var category = Category.newCategory("Filmes", "Filmes de todos os gêneros.", true);

        final var entity = CategoryJpaEntity.from(category);
        entity.setUpdatedAt(null);

        //Se passarmos o "createdAt" nulo, o Hibernate lançará uma exceção, pois o nome é uma propriedade que não pode ser nula.

        //O "DataIntegrityViolationException" é só uma exception genérica que o Spring usa para encapsular algum problema.
        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(entity));

        //A "PropertyValueException" é a exception que o Hibernate usa para validar as propriedades de uma entidade. É ela a exception
        //raiz, que é encapsulada pela "DataIntegrityViolationException", então estamos verificando se a causa da "DataIntegrityViolationException" foi
        //a "PropertyValueException".

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals("updatedAt", actualCause.getPropertyName());
        Assertions.assertEquals("not-null property references a null or transient value : com.projetopraticobackend.servicocatalogo.infrastructure.category.persistence.CategoryJpaEntity.updatedAt", actualCause.getMessage());
    }
}
