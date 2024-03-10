package com.projetopraticobackend.servicocatalogo.application;

/* Essa classe representará os casos de uso que não possuem nenhum tipo de retorno. */
public abstract class UnitUseCase<IN> {

    public abstract void execute(IN in);
}
