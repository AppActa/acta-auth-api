package br.com.acta.entity.core;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.acta.entity.base.auditoria.AuditoriaBase;
import br.com.acta.entity.core.contato.EmailColaborador;
import br.com.acta.entity.core.contato.TelefoneColaborador;
import br.com.acta.entity.enums.StatusGeral;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "colaborador", schema = "public")
@Getter @Setter @NoArgsConstructor
public class Colaborador extends AuditoriaBase {
    @Column(name = "cpf", length = 11, updatable = false, nullable = false, unique = true, columnDefinition = "CHAR(11)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private String cpf;

    @Column(name = "nome", length = 160, nullable = false)
    private String nome;

    @Column(name = "nickname", length = 160, nullable = false)
    private String nickname;

    @Column(name = "cargo", length = 100, nullable = false)
    private String cargo;

    @Column(name = "area", length = 100, nullable = false)
    private String area;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "data_contratacao", nullable = false)
    private LocalDate dataContratacao;

    @Column(name = "permissao_gestor", nullable = false)
    private boolean permissaoGestor;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusGeral status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false, updatable = false)
    private Empresa empresa;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmailColaborador> emails = new HashSet<>();

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TelefoneColaborador> telefones = new HashSet<>();
}