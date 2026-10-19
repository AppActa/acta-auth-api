package br.com.acta.entity.base;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EqualsAndHashCode(of = "id")
@Getter @Setter
public abstract class ModelBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}