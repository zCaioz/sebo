package br.com.uem.sebo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Venda.
 */
@Entity
@Table(name = "venda")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_venda", nullable = false)
    private LocalDate dataVenda;

    @NotNull
    @Column(name = "valor", precision = 21, scale = 2, nullable = false)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "emprestimos", "vendas" }, allowSetters = true)
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_venda__itens", joinColumns = @JoinColumn(name = "venda_id"), inverseJoinColumns = @JoinColumn(name = "itens_id"))
    @JsonIgnoreProperties(value = { "emprestimos", "vendas" }, allowSetters = true)
    private Set<Item> itens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venda id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVenda() {
        return this.dataVenda;
    }

    public Venda dataVenda(LocalDate dataVenda) {
        this.setDataVenda(dataVenda);
        return this;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public Venda valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Venda usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Set<Item> getItens() {
        return this.itens;
    }

    public void setItens(Set<Item> items) {
        this.itens = items;
    }

    public Venda itens(Set<Item> items) {
        this.setItens(items);
        return this;
    }

    public Venda addItens(Item item) {
        this.itens.add(item);
        return this;
    }

    public Venda removeItens(Item item) {
        this.itens.remove(item);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venda)) {
            return false;
        }
        return getId() != null && getId().equals(((Venda) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venda{" +
            "id=" + getId() +
            ", dataVenda='" + getDataVenda() + "'" +
            ", valor=" + getValor() +
            "}";
    }
}
