package br.com.acta.mapper;

import br.com.acta.common.utils.Formatador;
import br.com.acta.dto.core.colaborador.ColaboradorRequestDTO;
import br.com.acta.dto.core.colaborador.ColaboradorResponseDTO;
import br.com.acta.entity.core.Colaborador;
import br.com.acta.entity.core.contato.EmailColaborador;
import br.com.acta.entity.core.contato.TelefoneColaborador;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EmailMapper.class, TelefoneMapper.class, Formatador.class})
public interface ColaboradorMapper {
    @Mapping(source = "usuario.id", target = "idUsuario")
    @Mapping(source = "empresa.id", target = "idEmpresa")
    @Mapping(source = "cpf", target = "cpf", qualifiedByName = "formatarCpf")
    ColaboradorResponseDTO toResponse(Colaborador colaborador);

    @Mapping(source = "contato", target = "numero", qualifiedByName = "formatarTelefone")
    ColaboradorResponseDTO.TelefoneResponseDTO toTelefoneResponse(TelefoneColaborador telefone);

    @Mapping(source = "contato", target = "email")
    ColaboradorResponseDTO.EmailResponseDTO toEmailResponse(EmailColaborador email);

    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Colaborador toEntity(ColaboradorRequestDTO dto);

    @AfterMapping
    default void link(@MappingTarget Colaborador colaborador) {
        colaborador.getEmails().forEach(email -> email.setColaborador(colaborador));
        colaborador.getTelefones().forEach(telefone -> telefone.setColaborador(colaborador));
    }
}
