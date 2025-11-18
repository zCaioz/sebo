package br.com.uem.sebo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Livro.
 */
@Entity
@Table(name = "livro")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "editora")
    private String editora;

    @Column(name = "numero_paginas")
    private Integer numeroPaginas;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Livro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public Livro isbn(String isbn) {
        this.setIsbn(isbn);
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditora() {
        return this.editora;
    }

    public Livro editora(String editora) {
        this.setEditora(editora);
        return this;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Integer getNumeroPaginas() {
        return this.numeroPaginas;
    }

    public Livro numeroPaginas(Integer numeroPaginas) {
        this.setNumeroPaginas(numeroPaginas);
        return this;
    }

    public void setNumeroPaginas(Integer numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livro)) {
            return false;
        }
        return getId() != null && getId().equals(((Livro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livro{" +
            "id=" + getId() +
            ", isbn='" + getIsbn() + "'" +
            ", editora='" + getEditora() + "'" +
            ", numeroPaginas=" + getNumeroPaginas() +
            "}";
    }
}
