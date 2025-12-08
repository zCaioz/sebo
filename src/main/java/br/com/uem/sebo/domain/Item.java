package br.com.uem.sebo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "genero")
    private String genero;

    @Column(name = "autor_artista")
    private String autorArtista;

    @NotNull
    @Column(name = "disponibilidade", nullable = false)
    private Boolean disponibilidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "itens", "usuario" }, allowSetters = true)
    private Emprestimo emprestimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "itens", "usuario" }, allowSetters = true)
    private Venda venda;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Item id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Item titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAno() {
        return this.ano;
    }

    public Item ano(Integer ano) {
        this.setAno(ano);
        return this;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getGenero() {
        return this.genero;
    }

    public Item genero(String genero) {
        this.setGenero(genero);
        return this;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAutorArtista() {
        return this.autorArtista;
    }

    public Item autorArtista(String autorArtista) {
        this.setAutorArtista(autorArtista);
        return this;
    }

    public void setAutorArtista(String autorArtista) {
        this.autorArtista = autorArtista;
    }

    public Boolean getDisponibilidade() {
        return this.disponibilidade;
    }

    public Item disponibilidade(Boolean disponibilidade) {
        this.setDisponibilidade(disponibilidade);
        return this;
    }

    public void setDisponibilidade(Boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public Emprestimo getEmprestimo() {
        return this.emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public Item emprestimo(Emprestimo emprestimo) {
        this.setEmprestimo(emprestimo);
        return this;
    }

    public Venda getVenda() {
        return this.venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Item venda(Venda venda) {
        this.setVenda(venda);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return getId() != null && getId().equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", ano=" + getAno() +
            ", genero='" + getGenero() + "'" +
            ", autorArtista='" + getAutorArtista() + "'" +
            ", disponibilidade='" + getDisponibilidade() + "'" +
            "}";
    }
}
