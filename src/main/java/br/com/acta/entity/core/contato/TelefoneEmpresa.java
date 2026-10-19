package br.com.acta.entity.core.contato;

import br.com.acta.entity.base.ContatoBase;
import br.com.acta.entity.core.Empresa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "telefone_empresa", schema = "public", uniqueConstraints = @UniqueConstraint(name = "telefone_empresa_unique_0", columnNames = {"id_empresa", "numero_telefone"}))
@AttributeOverride(name = "contato", column = @Column(name = "numero_telefone", nullable = false,  length = 20))
@Getter
@Setter
@NoArgsConstructor
public class TelefoneEmpresa extends ContatoBase {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;
}