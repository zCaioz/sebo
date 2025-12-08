package br.com.uem.sebo.domain;

import static br.com.uem.sebo.domain.ItemTestSamples.*;
import static br.com.uem.sebo.domain.UsuarioTestSamples.*;
import static br.com.uem.sebo.domain.VendaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.uem.sebo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VendaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venda.class);
        Venda venda1 = getVendaSample1();
        Venda venda2 = new Venda();
        assertThat(venda1).isNotEqualTo(venda2);

        venda2.setId(venda1.getId());
        assertThat(venda1).isEqualTo(venda2);

        venda2 = getVendaSample2();
        assertThat(venda1).isNotEqualTo(venda2);
    }

    @Test
    void usuarioTest() {
        Venda venda = getVendaRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        venda.setUsuario(usuarioBack);
        assertThat(venda.getUsuario()).isEqualTo(usuarioBack);

        venda.usuario(null);
        assertThat(venda.getUsuario()).isNull();
    }

    @Test
    void itensTest() {
        Venda venda = getVendaRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        venda.addItens(itemBack);
        assertThat(venda.getItens()).containsOnly(itemBack);

        venda.removeItens(itemBack);
        assertThat(venda.getItens()).doesNotContain(itemBack);

        venda.itens(new HashSet<>(Set.of(itemBack)));
        assertThat(venda.getItens()).containsOnly(itemBack);

        venda.setItens(new HashSet<>());
        assertThat(venda.getItens()).doesNotContain(itemBack);
    }
}
