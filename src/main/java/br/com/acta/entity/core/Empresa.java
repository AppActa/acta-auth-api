package br.com.acta.entity.core;

import br.com.acta.entity.base.auditoria.AuditoriaBase;
import br.com.acta.entity.core.contato.EmailEmpresa;
import br.com.acta.entity.core.contato.TelefoneEmpresa;
import br.com.acta.entity.enums.StatusGeral;
import br.com.acta.entity.enums.TamanhoEmpresa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "empresa", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Empresa extends AuditoriaBase {
    @Column(name = "cnpj", nullable = false, length = 14, updatable = false, unique = true, columnDefinition = "CHAR(14)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private String cnpj;

    @Column(name = "nome", nullable = false, length = 160)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tamanho_empresa", nullable = false)
    private TamanhoEmpresa tamanho;

    @Column(name = "setor_empresa", nullable = false, length = 100)
    private String setor;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusGeral status;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmailEmpresa> emails = new HashSet<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TelefoneEmpresa> telefones = new HashSet<>();

}