package br.com.uem.sebo.repository;

import br.com.uem.sebo.domain.Emprestimo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Emprestimo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {}
