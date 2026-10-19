package br.com.acta.common.handler.exception;

public class RegexException extends RuntimeException {
    public RegexException(String message) {
        super("O " + message + " digitado é inválido");
    }
}