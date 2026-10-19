package br.com.acta.service;

import br.com.acta.common.client.BrevoClient;
import br.com.acta.common.config.firebase.FirebaseAuthFilter.FirebaseIdentity;
import br.com.acta.common.handler.exception.FirebaseAccessRevokedException;
import br.com.acta.common.handler.exception.UniqueViolationException;
import br.com.acta.common.utils.TokenUtils;
import br.com.acta.mapper.AuthMapper;
import br.com.acta.mapper.ConviteMapper;
import br.com.acta.dto.auth.ConviteRequestDTO;
import br.com.acta.dto.auth.MeResponseDTO;
import br.com.acta.mapper.ColaboradorMapper;
import br.com.acta.dto.core.colaborador.ColaboradorRequestDTO;
import br.com.acta.dto.core.colaborador.ColaboradorResponseDTO;
import br.com.acta.entity.core.Colaborador;
import br.com.acta.entity.core.Convite;
import br.com.acta.entity.core.Empresa;
import br.com.acta.entity.core.Usuario;
import br.com.acta.entity.enums.StatusGeral;
import br.com.acta.entity.enums.StatusConvite;
import br.com.acta.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConviteService {
    private static final int VALIDADE_MINUTOS = 30;

    private final UsuarioRepository usuarioRepo;
    private final ColaboradorRepository colaboradorRepo;
    private final EmpresaRepository empresaRepo;
    private final ConviteRepository repo;
    private final ColaboradorMapper colaboradorMapper;
    private final ConviteMapper mapper;
    private final AuthMapper authMapper;
    private final AuthService authService;
    private final BrevoClient brevoClient;
    private final EmailColaboradorRepository emailColaboradorRepo;
    private final TelefoneColaboradorRepository telefoneColaboradorRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @PreAuthorize("hasAuthority('ROLE_FIREBASE')")
    @Transactional
    public MeResponseDTO ativar(FirebaseIdentity identity, String token) {
        validarIdentidadeFirebase(identity);

        Convite convite = buscarConviteValido(token, identity);
        Usuario usuario = convite.getUsuario();
        Colaborador colaborador = buscarColaboradorPendente(usuario, identity.firebaseUid());

        ativarCadastro(usuario, colaborador, convite, identity.firebaseUid());

        return authMapper.toMeResponse(authMapper.toUsuarioAutenticado(usuario));
    }

    @PreAuthorize("hasRole('ADMIN') and @authService.isUsuarioByIdEmpresa(#idEmpresa)")
    @Transactional
    public ColaboradorResponseDTO convidar(Long idEmpresa, ConviteRequestDTO dto) {
        authService.configurarUsuarioAtual();

        Empresa empresa = empresaRepo.getEntity(idEmpresa, authService.atual().idEmpresa());
        Usuario usuario = buscarUsuarioParaConvite(dto, empresa);
        String codigo = TokenUtils.gerarCodigo();
        Convite convite = criarConvite(usuario, codigo);

        entityManager.flush();
        brevoClient.enviarConvite(convite.getEmailDestino(), usuario.getNome(), convite.getExpiraEm(), codigo);

        return colaboradorMapper.toResponse(usuario.getColaborador());
    }

    @PreAuthorize("hasRole('ADMIN') and @authService.isColaboradorEmpresa(#idColaborador)")
    @Transactional
    public void reenviar(Long idColaborador) {
        authService.configurarUsuarioAtual();
        Colaborador colaborador = colaboradorRepo.getEntity(idColaborador, authService.atual().idEmpresa());
        Usuario usuario = colaborador.getUsuario();

        if (colaborador.getStatus() != StatusGeral.PENDENTE || usuario.getStatus() != StatusGeral.PENDENTE)
            throw new UniqueViolationException("E-mail");

        List<Convite> convitesPendentes = repo.findAllByUsuarioIdAndStatusAndExpiraEmAfter(usuario.getId(), StatusConvite.PENDENTE, OffsetDateTime.now());
        convitesPendentes.forEach(convitePendente -> convitePendente.setStatus(StatusConvite.REVOGADO));

        String codigo = TokenUtils.gerarCodigo();
        Convite convite = criarConvite(usuario, codigo);

        entityManager.flush();
        brevoClient.enviarConvite(convite.getEmailDestino(), usuario.getNome(), convite.getExpiraEm(), codigo);
    }

    private void validarIdentidadeFirebase(FirebaseIdentity identity) {
        if (!identity.emailVerificado()) throw new FirebaseAccessRevokedException();
    }

    private Convite buscarConviteValido(String token, FirebaseIdentity identity) {
        Convite convite = repo.findByTokenHash(TokenUtils.hashToken(token)).orElseThrow(FirebaseAccessRevokedException::new);

        boolean valido = convite.getStatus() == StatusConvite.PENDENTE
                && convite.getExpiraEm().isAfter(OffsetDateTime.now())
                && convite.getEmailDestino().equalsIgnoreCase(identity.email())
                && convite.getUsuario().getEmailLogin().equalsIgnoreCase(identity.email());

        if (!valido) throw new FirebaseAccessRevokedException();
        return convite;
    }

    private Colaborador buscarColaboradorPendente(Usuario usuario, String firebaseUid) {
        if (usuario.getStatus() != StatusGeral.PENDENTE || usuario.getFirebaseUid() != null || usuarioRepo.existsByFirebaseUid(firebaseUid))
            throw new FirebaseAccessRevokedException();

        Colaborador colaborador = colaboradorRepo.findByUsuarioIdAndEmpresaId(usuario.getId(), usuario.getEmpresa().getId()).orElseThrow(FirebaseAccessRevokedException::new);

        if (colaborador.getStatus() != StatusGeral.PENDENTE) throw new FirebaseAccessRevokedException();

        return colaborador;
    }

    private void ativarCadastro(Usuario usuario, Colaborador colaborador, Convite convite, String firebaseUid) {
        entityManager
                .createNativeQuery("SELECT set_config('app.current_user_id', :id, true)")
                .setParameter("id", usuario.getId().toString()).getSingleResult();

        usuario.setFirebaseUid(firebaseUid);
        usuario.setStatus(StatusGeral.ATIVO);

        colaborador.setStatus(StatusGeral.ATIVO);

        convite.setStatus(StatusConvite.USADO);
        convite.setUsadoEm(OffsetDateTime.now());

        usuarioRepo.save(usuario);
        colaboradorRepo.save(colaborador);
        repo.save(convite);
    }

    private void validarDuplicidades(ConviteRequestDTO dto) {
        if (usuarioRepo.existsByEmailLoginIgnoreCase(dto.email())) throw new UniqueViolationException("E-mail");
        validarDuplicidadesColaborador(dto);
    }

    private void validarDuplicidadesColaborador(ConviteRequestDTO dto) {
        if (colaboradorRepo.existsByCpf(dto.cpf())) throw new UniqueViolationException("CPF");
        if (emailColaboradorRepo.existsByContatoIgnoreCase(dto.email())) throw new UniqueViolationException("E-mail");
        if (telefoneColaboradorRepo.existsByContatoIgnoreCase(dto.telefone())) throw new UniqueViolationException("Telefone");
    }

    private Usuario buscarUsuarioParaConvite(ConviteRequestDTO dto, Empresa empresa) {
        Usuario usuario = usuarioRepo.findByEmailLoginIgnoreCase(dto.email()).orElse(null);
        if (usuario == null) {
            validarDuplicidades(dto);

            usuario = mapper.toUsuarioPendente(dto, empresa);
            Usuario salvo = usuarioRepo.save(usuario);

            Colaborador colaborador = criarColaboradorPendente(dto, empresa, usuario);
            salvo.setColaborador(colaborador);
            return salvo;
        }

        validarEmpresaUsuario(usuario, empresa);
        Colaborador colaborador = colaboradorRepo.findByUsuarioIdAndEmpresaId(usuario.getId(), empresa.getId()).orElse(null);

        if (colaborador == null) {
            validarDuplicidadesColaborador(dto);
            usuario.setColaborador(criarColaboradorPendente(dto, empresa, usuario));
        }
        else if (colaborador.getStatus() != StatusGeral.PENDENTE) throw new UniqueViolationException("E-mail");
        else usuario.setColaborador(colaborador);

        return usuario;
    }

    private void validarEmpresaUsuario(Usuario usuario, Empresa empresa) {
        if (!usuario.getEmpresa().getId().equals(empresa.getId())) throw new UniqueViolationException("E-mail");
    }

    private Colaborador criarColaboradorPendente(ConviteRequestDTO dto, Empresa empresa, Usuario usuario) {
        ColaboradorRequestDTO request = mapper.toColaboradorRequest(dto);
        Colaborador colaborador = colaboradorMapper.toEntity(request);

        colaborador.setEmpresa(empresa);
        colaborador.setUsuario(usuario);
        colaborador.setStatus(StatusGeral.PENDENTE);

        return colaboradorRepo.save(colaborador);
    }

    private Convite criarConvite(Usuario usuario, String token) {
        if (repo.existsByUsuarioIdAndStatusAndExpiraEmAfter(usuario.getId(), StatusConvite.PENDENTE, OffsetDateTime.now()))
            throw new UniqueViolationException("convite pendente válido");

        Convite convite = new Convite();

        convite.setUsuario(usuario);
        convite.setEmailDestino(usuario.getEmailLogin());
        convite.setTokenHash(TokenUtils.hashToken(token));
        convite.setStatus(StatusConvite.PENDENTE);
        convite.setExpiraEm(OffsetDateTime.now().plusMinutes(VALIDADE_MINUTOS));
        convite.setCriadoPor(usuarioRepo.getEntity(authService.atual().idUsuario(), authService.atual().idEmpresa()));

        return repo.save(convite);
    }
}