package br.com.acta.mapper;

import br.com.acta.dto.core.contato.email.EmailRequestDTO;
import br.com.acta.entity.core.contato.EmailColaborador;
import br.com.acta.entity.core.contato.EmailEmpresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    @Mapping(target = "contato", source = "email")
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    EmailColaborador toColaborador(EmailRequestDTO dto);

    @Mapping(target = "contato", source = "email")
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    EmailEmpresa toEmpresa(EmailRequestDTO dto);
}
