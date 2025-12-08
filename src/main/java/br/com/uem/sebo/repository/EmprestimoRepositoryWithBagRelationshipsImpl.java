package br.com.uem.sebo.repository;

import br.com.uem.sebo.domain.Emprestimo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EmprestimoRepositoryWithBagRelationshipsImpl implements EmprestimoRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String EMPRESTIMOS_PARAMETER = "emprestimos";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Emprestimo> fetchBagRelationships(Optional<Emprestimo> emprestimo) {
        return emprestimo.map(this::fetchItens);
    }

    @Override
    public Page<Emprestimo> fetchBagRelationships(Page<Emprestimo> emprestimos) {
        return new PageImpl<>(fetchBagRelationships(emprestimos.getContent()), emprestimos.getPageable(), emprestimos.getTotalElements());
    }

    @Override
    public List<Emprestimo> fetchBagRelationships(List<Emprestimo> emprestimos) {
        return Optional.of(emprestimos).map(this::fetchItens).orElse(Collections.emptyList());
    }

    Emprestimo fetchItens(Emprestimo result) {
        return entityManager
            .createQuery(
                "select emprestimo from Emprestimo emprestimo left join fetch emprestimo.itens where emprestimo.id = :id",
                Emprestimo.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Emprestimo> fetchItens(List<Emprestimo> emprestimos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, emprestimos.size()).forEach(index -> order.put(emprestimos.get(index).getId(), index));
        List<Emprestimo> result = entityManager
            .createQuery(
                "select emprestimo from Emprestimo emprestimo left join fetch emprestimo.itens where emprestimo in :emprestimos",
                Emprestimo.class
            )
            .setParameter(EMPRESTIMOS_PARAMETER, emprestimos)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
