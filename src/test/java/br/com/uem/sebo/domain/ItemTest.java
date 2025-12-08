package br.com.uem.sebo.domain;

import static br.com.uem.sebo.domain.EmprestimoTestSamples.*;
import static br.com.uem.sebo.domain.ItemTestSamples.*;
import static br.com.uem.sebo.domain.VendaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.uem.sebo.web.rest.TestUtil;
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

        item.setEmprestimo(emprestimoBack);
        assertThat(item.getEmprestimo()).isEqualTo(emprestimoBack);

        item.emprestimo(null);
        assertThat(item.getEmprestimo()).isNull();
    }

    @Test
    void vendaTest() {
        Item item = getItemRandomSampleGenerator();
        Venda vendaBack = getVendaRandomSampleGenerator();

        item.setVenda(vendaBack);
        assertThat(item.getVenda()).isEqualTo(vendaBack);

        item.venda(null);
        assertThat(item.getVenda()).isNull();
    }
}
