package br.com.acta.mapper;

import br.com.acta.dto.auth.ConviteRequestDTO;
import br.com.acta.dto.core.colaborador.ColaboradorRequestDTO;
import br.com.acta.entity.core.Empresa;
import br.com.acta.entity.core.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConviteMapper {
    @Mapping(target = "nome", source = "dto.nome")
    @Mapping(target = "emailLogin", source = "dto.email")
    @Mapping(target = "tipo", source = "dto.tipo")
    @Mapping(target = "status", constant = "PENDENTE")
    @Mapping(target = "empresa", source = "empresa")
    @Mapping(target = "firebaseUid", ignore = true)
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Usuario toUsuarioPendente(ConviteRequestDTO dto, Empresa empresa);

    @Mapping(target = "emails", expression = "java(List.of(new EmailRequestDTO(request.email(), true)))")
    @Mapping(target = "telefones", expression = "java(List.of(new TelefoneRequestDTO(request.telefone(), true)))")
    @Mapping(target = "usuario", expression = "java(new UsuarioRequestDTO(request.nome(), request.email(), null, request.tipo()))")
    @Mapping(target = "nickname", ignore = true)
    ColaboradorRequestDTO toColaboradorRequest(ConviteRequestDTO request);
}
