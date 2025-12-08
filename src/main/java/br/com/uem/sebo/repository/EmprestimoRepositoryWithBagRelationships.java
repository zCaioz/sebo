package br.com.uem.sebo.repository;

import br.com.uem.sebo.domain.Emprestimo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface EmprestimoRepositoryWithBagRelationships {
    Optional<Emprestimo> fetchBagRelationships(Optional<Emprestimo> emprestimo);

    List<Emprestimo> fetchBagRelationships(List<Emprestimo> emprestimos);

    Page<Emprestimo> fetchBagRelationships(Page<Emprestimo> emprestimos);
}
