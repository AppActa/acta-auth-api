package br.com.acta.mapper;

import br.com.acta.entity.core.Usuario;
import br.com.acta.dto.core.usuario.UsuarioRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "emailLogin", source = "email")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);
}
