package br.com.acta.entity.base.auditoria;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class AuditoriaBase extends ImutavelBase {
    @Column(name = "atualizado_em")
    @UpdateTimestamp
    private OffsetDateTime atualizadoEm;
}