package com.projetopraticobackend.servicocatalogo.domain.exceptions;

/* Essa stacktrace será utilizada quando não queremos armazenar registros dessa
* stacktrace. Assim, essas exceptions onerarão muito menos a aplicação, pois
* o Java, para coletar a stacktrace, precisa parar a thread atual, e isso consome
* recursos. */
public class NoStacktraceException extends RuntimeException {

    public NoStacktraceException(final String message) {
        this(message, null);
    }

    public NoStacktraceException (final String message, final Throwable cause) {
        super(message, cause, true, false); //Os dois últimos parâmetros são para que a stacktrace não seja impressa de forma completa. Eles auxiliam a performance da aplicação.
    }
}
