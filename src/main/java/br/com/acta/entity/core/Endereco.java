package br.com.acta.entity.core;

import br.com.acta.entity.base.auditoria.AuditoriaBase;
import br.com.acta.entity.enums.UF;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "endereco_empresa", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Endereco extends AuditoriaBase {
    @Column(name = "cep", length = 8, nullable = false, columnDefinition = "CHAR(8)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private String cep;

    @Column(name = "uf", length = 2, nullable = false, columnDefinition = "CHAR(2)")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UF uf;

    @Column(name = "cidade", length = 100, nullable = false)
    private String cidade;

    @Column(name = "bairro", length = 100, nullable = false)
    private String bairro;

    @Column(name = "logradouro", length = 180, nullable = false)
    private String logradouro;

    @Column(name = "numero_endereco", length = 20, nullable = false)
    private String numero;

    @Column(name = "complemento", columnDefinition = "TEXT")
    private String complemento;

    @Column(name = "principal", nullable = false)
    private Boolean principal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;
}