package br.com.uem.sebo.repository;

import br.com.uem.sebo.domain.Venda;
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
public class VendaRepositoryWithBagRelationshipsImpl implements VendaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String VENDAS_PARAMETER = "vendas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Venda> fetchBagRelationships(Optional<Venda> venda) {
        return venda.map(this::fetchItens);
    }

    @Override
    public Page<Venda> fetchBagRelationships(Page<Venda> vendas) {
        return new PageImpl<>(fetchBagRelationships(vendas.getContent()), vendas.getPageable(), vendas.getTotalElements());
    }

    @Override
    public List<Venda> fetchBagRelationships(List<Venda> vendas) {
        return Optional.of(vendas).map(this::fetchItens).orElse(Collections.emptyList());
    }

    Venda fetchItens(Venda result) {
        return entityManager
            .createQuery("select venda from Venda venda left join fetch venda.itens where venda.id = :id", Venda.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Venda> fetchItens(List<Venda> vendas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, vendas.size()).forEach(index -> order.put(vendas.get(index).getId(), index));
        List<Venda> result = entityManager
            .createQuery("select venda from Venda venda left join fetch venda.itens where venda in :vendas", Venda.class)
            .setParameter(VENDAS_PARAMETER, vendas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
