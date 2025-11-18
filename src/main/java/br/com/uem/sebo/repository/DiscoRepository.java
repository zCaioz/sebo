package br.com.uem.sebo.repository;

import br.com.uem.sebo.domain.Disco;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Disco entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscoRepository extends JpaRepository<Disco, Long> {}
