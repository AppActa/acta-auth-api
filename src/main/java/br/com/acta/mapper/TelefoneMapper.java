package br.com.acta.mapper;

import br.com.acta.dto.core.contato.TelefoneRequestDTO;
import br.com.acta.entity.core.contato.TelefoneColaborador;
import br.com.acta.entity.core.contato.TelefoneEmpresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TelefoneMapper {
    @Mapping(target = "contato", source = "numero")
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    TelefoneColaborador toColaborador(TelefoneRequestDTO dto);

    @Mapping(target = "contato", source = "numero")
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    TelefoneEmpresa toEmpresa(TelefoneRequestDTO dto);
}
