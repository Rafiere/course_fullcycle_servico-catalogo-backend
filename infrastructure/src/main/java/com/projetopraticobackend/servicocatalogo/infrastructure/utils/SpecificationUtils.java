package com.projetopraticobackend.servicocatalogo.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

/* Para ser uma classe "util", ela deve ser privada, ou seja, os métodos dela devem
 * ser privados e, além disso, criamos um construtor vazio privado, para que, se alguém
 * force, ele não instancie a classe, e todos os métodos são estáticos. */
public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> like(final String prop, //É a propriedade que queremos filtrar.
                                            final String term) { //É o termo que queremos filtrar.

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.upper(root.get(prop)), //É a propriedade que queremos filtrar. Estamos pegando o caminho até a propriedade com o "root.get()" e convertendo a propriedade para o uppercase, além de também convertermos o termo para "uppercase".
                "%" + term.toUpperCase() + "%"); //É o termo que queremos filtrar.
    }
}