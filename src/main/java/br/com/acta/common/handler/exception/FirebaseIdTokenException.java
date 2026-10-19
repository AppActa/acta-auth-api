package br.com.acta.common.handler.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class FirebaseIdTokenException extends BadCredentialsException {
    public FirebaseIdTokenException() {
        super("O ID Token do Firebase enviado não é válido");
    }
}