package br.com.uem.sebo.domain;

import static br.com.uem.sebo.domain.LivroTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.uem.sebo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Livro.class);
        Livro livro1 = getLivroSample1();
        Livro livro2 = new Livro();
        assertThat(livro1).isNotEqualTo(livro2);

        livro2.setId(livro1.getId());
        assertThat(livro1).isEqualTo(livro2);

        livro2 = getLivroSample2();
        assertThat(livro1).isNotEqualTo(livro2);
    }
}
