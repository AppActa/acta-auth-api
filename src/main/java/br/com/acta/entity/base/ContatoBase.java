package br.com.acta.entity.base;

import br.com.acta.entity.base.auditoria.ImutavelBase;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class ContatoBase extends ImutavelBase {
    @Column(nullable = false)
    private String contato;

    @Column(name = "principal", nullable = false)
    private Boolean principal;
}