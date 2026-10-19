package br.com.acta.entity.core;

import br.com.acta.entity.base.auditoria.AuditoriaBase;
import br.com.acta.entity.enums.StatusGeral;
import br.com.acta.entity.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_sistema", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Usuario extends AuditoriaBase {
    @Column(name = "nome", length = 160, nullable = false)
    private String nome;

    @Column(name = "email_login", length = 254, nullable = false, unique = true)
    private String emailLogin;

    @Column(name = "firebase_uid", unique = true, length = 128)
    private String firebaseUid;

    @Column(name = "tipo_usuario", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusGeral status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Colaborador colaborador;

}