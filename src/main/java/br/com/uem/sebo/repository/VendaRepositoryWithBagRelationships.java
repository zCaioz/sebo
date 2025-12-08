package br.com.uem.sebo.repository;

import br.com.uem.sebo.domain.Venda;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface VendaRepositoryWithBagRelationships {
    Optional<Venda> fetchBagRelationships(Optional<Venda> venda);

    List<Venda> fetchBagRelationships(List<Venda> vendas);

    Page<Venda> fetchBagRelationships(Page<Venda> vendas);
}
