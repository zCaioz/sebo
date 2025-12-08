package br.com.uem.sebo.domain;

import static br.com.uem.sebo.domain.EmprestimoTestSamples.*;
import static br.com.uem.sebo.domain.ItemTestSamples.*;
import static br.com.uem.sebo.domain.UsuarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.uem.sebo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmprestimoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Emprestimo.class);
        Emprestimo emprestimo1 = getEmprestimoSample1();
        Emprestimo emprestimo2 = new Emprestimo();
        assertThat(emprestimo1).isNotEqualTo(emprestimo2);

        emprestimo2.setId(emprestimo1.getId());
        assertThat(emprestimo1).isEqualTo(emprestimo2);

        emprestimo2 = getEmprestimoSample2();
        assertThat(emprestimo1).isNotEqualTo(emprestimo2);
    }

    @Test
    void itensTest() {
        Emprestimo emprestimo = getEmprestimoRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        emprestimo.addItens(itemBack);
        assertThat(emprestimo.getItens()).containsOnly(itemBack);
        assertThat(itemBack.getEmprestimo()).isEqualTo(emprestimo);

        emprestimo.removeItens(itemBack);
        assertThat(emprestimo.getItens()).doesNotContain(itemBack);
        assertThat(itemBack.getEmprestimo()).isNull();

        emprestimo.itens(new HashSet<>(Set.of(itemBack)));
        assertThat(emprestimo.getItens()).containsOnly(itemBack);
        assertThat(itemBack.getEmprestimo()).isEqualTo(emprestimo);

        emprestimo.setItens(new HashSet<>());
        assertThat(emprestimo.getItens()).doesNotContain(itemBack);
        assertThat(itemBack.getEmprestimo()).isNull();
    }

    @Test
    void usuarioTest() {
        Emprestimo emprestimo = getEmprestimoRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        emprestimo.setUsuario(usuarioBack);
        assertThat(emprestimo.getUsuario()).isEqualTo(usuarioBack);

        emprestimo.usuario(null);
        assertThat(emprestimo.getUsuario()).isNull();
    }
}
