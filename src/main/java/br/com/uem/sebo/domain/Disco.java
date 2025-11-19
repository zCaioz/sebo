package br.com.uem.sebo.domain;

import br.com.uem.sebo.domain.enumeration.TipoMidia;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * A Disco.
 */
@Entity
@Table(name = "disco")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Disco extends Item {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_midia", nullable = false)
    private TipoMidia tipoMidia;

    @Column(name = "duracao_minutos")
    private Integer duracaoMinutos;

    public TipoMidia getTipoMidia() {
        return this.tipoMidia;
    }

    public Disco tipoMidia(TipoMidia tipoMidia) {
        this.setTipoMidia(tipoMidia);
        return this;
    }

    public void setTipoMidia(TipoMidia tipoMidia) {
        this.tipoMidia = tipoMidia;
    }

    public Integer getDuracaoMinutos() {
        return this.duracaoMinutos;
    }

    public Disco duracaoMinutos(Integer duracaoMinutos) {
        this.setDuracaoMinutos(duracaoMinutos);
        return this;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Disco)) {
            return false;
        }
        return getId() != null && getId().equals(((Disco) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Disco{" +
                "id=" + getId() +
                ", tipoMidia='" + getTipoMidia() + "'" +
                ", duracaoMinutos=" + getDuracaoMinutos() +
                "}";
    }
}
