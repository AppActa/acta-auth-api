package br.com.acta.entity.base.auditoria;

import br.com.acta.entity.base.ModelBase;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class ImutavelBase extends ModelBase {
    @Column(name = "criado_em", nullable = false, updatable = false)
    @CreationTimestamp
    private OffsetDateTime criadoEm;
}