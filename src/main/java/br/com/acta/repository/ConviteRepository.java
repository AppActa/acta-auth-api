package br.com.acta.repository;

import br.com.acta.entity.core.Convite;
import br.com.acta.entity.enums.StatusConvite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface ConviteRepository extends JpaRepository<Convite, Long> {
    Optional<Convite> findByTokenHash(String tokenHash);
    List<Convite> findAllByUsuarioIdAndStatusAndExpiraEmAfter(Long idUsuario, StatusConvite status, OffsetDateTime agora);
    boolean existsByUsuarioIdAndStatusAndExpiraEmAfter(Long idUsuario, StatusConvite status, OffsetDateTime agora);
}
