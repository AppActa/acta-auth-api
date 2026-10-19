package br.com.acta.mapper;

import br.com.acta.entity.core.Endereco;
import br.com.acta.dto.core.empresa.endereco.EnderecoRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Endereco toEntity(EnderecoRequestDTO dto);
}
