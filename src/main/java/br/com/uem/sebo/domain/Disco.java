package br.com.uem.sebo.domain;

import br.com.uem.sebo.domain.enumeration.TipoMidia;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Disco.
 */
@Entity
@Table(name = "disco")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Disco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_midia", nullable = false)
    private TipoMidia tipoMidia;

    @Column(name = "duracao_minutos")
    private Integer duracaoMinutos;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Disco id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
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
