package br.com.acta.common.handler.exception;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

public class ModelNotFoundException extends EntityNotFoundException {
    public ModelNotFoundException(String classe) {
        super(classe + " não pode ser encontrado");
    }

    public ModelNotFoundException(String classe, Long id) {
        super(classe + " não pode ser encontrado com o ID " + id);
    }

    public ModelNotFoundException(String classe, List<Long> ids) {
        super(classe + " não pode ser encontrado com os IDs " + ids);
    }
}