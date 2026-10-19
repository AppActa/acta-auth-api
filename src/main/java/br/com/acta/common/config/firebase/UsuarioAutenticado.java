package br.com.acta.common.config.firebase;

import br.com.acta.entity.enums.StatusGeral;
import br.com.acta.entity.enums.TipoUsuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public record UsuarioAutenticado(
        String firebaseUid,
        Long idUsuario,
        Long idEmpresa,
        Long idColaborador,
        String nome,
        String email,
        String nomeEmpresa,
        TipoUsuario tipo,
        boolean permissaoGestor,
        StatusGeral status
) implements Principal {
    // converte papel e permissões do usuário em authorities
    public List<GrantedAuthority> authorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + tipo.name()));

        if (permissaoGestor)
            authorities.add(new SimpleGrantedAuthority("PERMISSAO_GESTOR"));

        return authorities;
    }

    @Override
    public String getName() {
        return firebaseUid;
    }
}