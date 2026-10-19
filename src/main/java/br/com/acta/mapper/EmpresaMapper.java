package br.com.acta.mapper;

import br.com.acta.dto.core.empresa.EmpresaRequestDTO;
import br.com.acta.entity.core.Empresa;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class, EmailMapper.class, TelefoneMapper.class})
public interface EmpresaMapper {
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Empresa toEntity(EmpresaRequestDTO dto);

    @AfterMapping
    default void link(@MappingTarget Empresa empresa) {
        empresa.getEmails().forEach(email -> email.setEmpresa(empresa));
        empresa.getTelefones().forEach(telefone -> telefone.setEmpresa(empresa));
        empresa.getEnderecos().forEach(endereco -> endereco.setEmpresa(empresa));
    }
}
