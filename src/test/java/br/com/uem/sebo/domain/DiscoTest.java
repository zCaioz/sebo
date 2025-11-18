package br.com.uem.sebo.domain;

import static br.com.uem.sebo.domain.DiscoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.uem.sebo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DiscoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disco.class);
        Disco disco1 = getDiscoSample1();
        Disco disco2 = new Disco();
        assertThat(disco1).isNotEqualTo(disco2);

        disco2.setId(disco1.getId());
        assertThat(disco1).isEqualTo(disco2);

        disco2 = getDiscoSample2();
        assertThat(disco1).isNotEqualTo(disco2);
    }
}
