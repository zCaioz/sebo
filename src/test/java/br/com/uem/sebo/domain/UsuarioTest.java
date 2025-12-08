package br.com.uem.sebo.domain;

import static br.com.uem.sebo.domain.EmprestimoTestSamples.*;
import static br.com.uem.sebo.domain.UsuarioTestSamples.*;
import static br.com.uem.sebo.domain.VendaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.uem.sebo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Usuario.class);
        Usuario usuario1 = getUsuarioSample1();
        Usuario usuario2 = new Usuario();
        assertThat(usuario1).isNotEqualTo(usuario2);

        usuario2.setId(usuario1.getId());
        assertThat(usuario1).isEqualTo(usuario2);

        usuario2 = getUsuarioSample2();
        assertThat(usuario1).isNotEqualTo(usuario2);
    }

    @Test
    void emprestimosTest() {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        Emprestimo emprestimoBack = getEmprestimoRandomSampleGenerator();

        usuario.addEmprestimos(emprestimoBack);
        assertThat(usuario.getEmprestimos()).containsOnly(emprestimoBack);
        assertThat(emprestimoBack.getUsuario()).isEqualTo(usuario);

        usuario.removeEmprestimos(emprestimoBack);
        assertThat(usuario.getEmprestimos()).doesNotContain(emprestimoBack);
        assertThat(emprestimoBack.getUsuario()).isNull();

        usuario.emprestimos(new HashSet<>(Set.of(emprestimoBack)));
        assertThat(usuario.getEmprestimos()).containsOnly(emprestimoBack);
        assertThat(emprestimoBack.getUsuario()).isEqualTo(usuario);

        usuario.setEmprestimos(new HashSet<>());
        assertThat(usuario.getEmprestimos()).doesNotContain(emprestimoBack);
        assertThat(emprestimoBack.getUsuario()).isNull();
    }

    @Test
    void vendasTest() {
        Usuario usuario = getUsuarioRandomSampleGenerator();
        Venda vendaBack = getVendaRandomSampleGenerator();

        usuario.addVendas(vendaBack);
        assertThat(usuario.getVendas()).containsOnly(vendaBack);
        assertThat(vendaBack.getUsuario()).isEqualTo(usuario);

        usuario.removeVendas(vendaBack);
        assertThat(usuario.getVendas()).doesNotContain(vendaBack);
        assertThat(vendaBack.getUsuario()).isNull();

        usuario.vendas(new HashSet<>(Set.of(vendaBack)));
        assertThat(usuario.getVendas()).containsOnly(vendaBack);
        assertThat(vendaBack.getUsuario()).isEqualTo(usuario);

        usuario.setVendas(new HashSet<>());
        assertThat(usuario.getVendas()).doesNotContain(vendaBack);
        assertThat(vendaBack.getUsuario()).isNull();
    }
}
