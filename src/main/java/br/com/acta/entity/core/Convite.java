package br.com.acta.entity.core;

import br.com.acta.entity.base.auditoria.ImutavelBase;
import br.com.acta.entity.enums.StatusConvite;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "convite_usuario", schema = "public")
@Getter @Setter @NoArgsConstructor
public class Convite extends ImutavelBase {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "email_destino", length = 254, nullable = false)
    private String emailDestino;

    @Column(name = "token_hash", length = 255, nullable = false, unique = true)
    private String tokenHash;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusConvite status;

    @Column(name = "expira_em", nullable = false)
    private OffsetDateTime expiraEm;

    @Column(name = "usado_em")
    private OffsetDateTime usadoEm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "criado_por", nullable = false)
    private Usuario criadoPor;
}