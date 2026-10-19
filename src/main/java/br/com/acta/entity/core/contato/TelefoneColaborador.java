package br.com.acta.entity.core.contato;

import br.com.acta.entity.base.ContatoBase;
import br.com.acta.entity.core.Colaborador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "telefone_colaborador", schema = "public", uniqueConstraints = @UniqueConstraint(name = "telefone_colaborador_unique_0", columnNames = {"id_colaborador", "numero_telefone"}))
@AttributeOverride(name = "contato", column = @Column(name = "numero_telefone", nullable = false, length = 20))
@Getter
@Setter
@NoArgsConstructor
public class TelefoneColaborador extends ContatoBase {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_colaborador", nullable = false)
    private Colaborador colaborador;
}