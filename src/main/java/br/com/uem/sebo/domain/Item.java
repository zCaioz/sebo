package br.com.uem.sebo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Inheritance(strategy = InheritanceType.JOINED)
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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "itens")
    @JsonIgnoreProperties(value = { "usuario", "itens" }, allowSetters = true)
    private Set<Emprestimo> emprestimos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "itens")
    @JsonIgnoreProperties(value = { "usuario", "itens" }, allowSetters = true)
    private Set<Venda> vendas = new HashSet<>();

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

    public Set<Emprestimo> getEmprestimos() {
        return this.emprestimos;
    }

    public void setEmprestimos(Set<Emprestimo> emprestimos) {
        if (this.emprestimos != null) {
            this.emprestimos.forEach(i -> i.removeItens(this));
        }
        if (emprestimos != null) {
            emprestimos.forEach(i -> i.addItens(this));
        }
        this.emprestimos = emprestimos;
    }

    public Item emprestimos(Set<Emprestimo> emprestimos) {
        this.setEmprestimos(emprestimos);
        return this;
    }

    public Item addEmprestimo(Emprestimo emprestimo) {
        this.emprestimos.add(emprestimo);
        emprestimo.getItens().add(this);
        return this;
    }

    public Item removeEmprestimo(Emprestimo emprestimo) {
        this.emprestimos.remove(emprestimo);
        emprestimo.getItens().remove(this);
        return this;
    }

    public Set<Venda> getVendas() {
        return this.vendas;
    }

    public void setVendas(Set<Venda> vendas) {
        if (this.vendas != null) {
            this.vendas.forEach(i -> i.removeItens(this));
        }
        if (vendas != null) {
            vendas.forEach(i -> i.addItens(this));
        }
        this.vendas = vendas;
    }

    public Item vendas(Set<Venda> vendas) {
        this.setVendas(vendas);
        return this;
    }

    public Item addVenda(Venda venda) {
        this.vendas.add(venda);
        venda.getItens().add(this);
        return this;
    }

    public Item removeVenda(Venda venda) {
        this.vendas.remove(venda);
        venda.getItens().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

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
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
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
