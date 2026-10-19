package br.com.acta.common.handler.exception;

public class UniqueViolationException extends RuntimeException {
    public UniqueViolationException(String recurso) {
        super("Já existe um registro para " + recurso);
    }

    public UniqueViolationException(String primeiroRecurso, String segundoRecurso){
        super("Já existe um vínculo entre " + primeiroRecurso + " e " + segundoRecurso);
    }
}