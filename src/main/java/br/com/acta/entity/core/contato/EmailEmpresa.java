package br.com.acta.entity.core.contato;

import br.com.acta.entity.core.Empresa;
import br.com.acta.entity.base.ContatoBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "email_empresa", schema = "public", uniqueConstraints = @UniqueConstraint(name = "email_empresa_unique_0", columnNames = {"id_empresa", "email"}))
@AttributeOverride(name = "contato", column = @Column(name = "email", nullable = false, length = 254))
@Getter
@Setter
@NoArgsConstructor
public class EmailEmpresa extends ContatoBase {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;
}