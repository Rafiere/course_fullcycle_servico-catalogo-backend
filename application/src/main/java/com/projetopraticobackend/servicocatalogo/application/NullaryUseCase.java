package com.projetopraticobackend.servicocatalogo.application;

/* Esse tipo de usecase não recebe nada e apenas retorna um valor. */
public abstract class NullaryUseCase<OUT> {

    public abstract OUT execute();
}