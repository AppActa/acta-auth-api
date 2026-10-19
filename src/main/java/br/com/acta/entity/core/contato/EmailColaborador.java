package br.com.acta.entity.core.contato;

import br.com.acta.entity.core.Colaborador;
import br.com.acta.entity.base.ContatoBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "email_colaborador", schema = "public", uniqueConstraints = @UniqueConstraint(name = "email_colaborador_unique_0", columnNames = {"id_colaborador", "email"}))
@AttributeOverride(name = "contato", column = @Column(name = "email", nullable = false, length = 254))
@Getter
@Setter
@NoArgsConstructor
public class EmailColaborador extends ContatoBase {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_colaborador", nullable = false)
    private Colaborador colaborador;
}