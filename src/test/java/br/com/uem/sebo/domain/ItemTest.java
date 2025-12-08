package br.com.uem.sebo.domain;

import static br.com.uem.sebo.domain.EmprestimoTestSamples.*;
import static br.com.uem.sebo.domain.ItemTestSamples.*;
import static br.com.uem.sebo.domain.VendaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.uem.sebo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Item.class);
        Item item1 = getItemSample1();
        Item item2 = new Item();
        assertThat(item1).isNotEqualTo(item2);

        item2.setId(item1.getId());
        assertThat(item1).isEqualTo(item2);

        item2 = getItemSample2();
        assertThat(item1).isNotEqualTo(item2);
    }

    @Test
    void emprestimoTest() {
        Item item = getItemRandomSampleGenerator();
        Emprestimo emprestimoBack = getEmprestimoRandomSampleGenerator();

        item.addEmprestimo(emprestimoBack);
        assertThat(item.getEmprestimos()).containsOnly(emprestimoBack);
        assertThat(emprestimoBack.getItens()).containsOnly(item);

        item.removeEmprestimo(emprestimoBack);
        assertThat(item.getEmprestimos()).doesNotContain(emprestimoBack);
        assertThat(emprestimoBack.getItens()).doesNotContain(item);

        item.emprestimos(new HashSet<>(Set.of(emprestimoBack)));
        assertThat(item.getEmprestimos()).containsOnly(emprestimoBack);
        assertThat(emprestimoBack.getItens()).containsOnly(item);

        item.setEmprestimos(new HashSet<>());
        assertThat(item.getEmprestimos()).doesNotContain(emprestimoBack);
        assertThat(emprestimoBack.getItens()).doesNotContain(item);
    }

    @Test
    void vendaTest() {
        Item item = getItemRandomSampleGenerator();
        Venda vendaBack = getVendaRandomSampleGenerator();

        item.addVenda(vendaBack);
        assertThat(item.getVendas()).containsOnly(vendaBack);
        assertThat(vendaBack.getItens()).containsOnly(item);

        item.removeVenda(vendaBack);
        assertThat(item.getVendas()).doesNotContain(vendaBack);
        assertThat(vendaBack.getItens()).doesNotContain(item);

        item.vendas(new HashSet<>(Set.of(vendaBack)));
        assertThat(item.getVendas()).containsOnly(vendaBack);
        assertThat(vendaBack.getItens()).containsOnly(item);

        item.setVendas(new HashSet<>());
        assertThat(item.getVendas()).doesNotContain(vendaBack);
        assertThat(vendaBack.getItens()).doesNotContain(item);
    }
}
