package br.com.acta.common.handler.exception;

import org.springframework.security.access.AccessDeniedException;

public class FirebaseAccessRevokedException extends AccessDeniedException {
    public FirebaseAccessRevokedException() {
        super("O ID Token enviado não é válido com nenhum usuário do ACTA");
    }
}