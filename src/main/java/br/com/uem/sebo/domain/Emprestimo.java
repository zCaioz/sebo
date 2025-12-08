package br.com.uem.sebo.domain;

import br.com.uem.sebo.domain.enumeration.StatusEmprestimo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Emprestimo.
 */
@Entity
@Table(name = "emprestimo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Emprestimo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @NotNull
    @Column(name = "data_prevista_devolucao", nullable = false)
    private LocalDate dataPrevistaDevolucao;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEmprestimo status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emprestimo")
    @JsonIgnoreProperties(value = { "emprestimo", "venda" }, allowSetters = true)
    private Set<Item> itens = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "emprestimos", "vendas" }, allowSetters = true)
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Emprestimo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataEmprestimo() {
        return this.dataEmprestimo;
    }

    public Emprestimo dataEmprestimo(LocalDate dataEmprestimo) {
        this.setDataEmprestimo(dataEmprestimo);
        return this;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return this.dataPrevistaDevolucao;
    }

    public Emprestimo dataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.setDataPrevistaDevolucao(dataPrevistaDevolucao);
        return this;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return this.dataDevolucao;
    }

    public Emprestimo dataDevolucao(LocalDate dataDevolucao) {
        this.setDataDevolucao(dataDevolucao);
        return this;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public StatusEmprestimo getStatus() {
        return this.status;
    }

    public Emprestimo status(StatusEmprestimo status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }

    public Set<Item> getItens() {
        return this.itens;
    }

    public void setItens(Set<Item> items) {
        if (this.itens != null) {
            this.itens.forEach(i -> i.setEmprestimo(null));
        }
        if (items != null) {
            items.forEach(i -> i.setEmprestimo(this));
        }
        this.itens = items;
    }

    public Emprestimo itens(Set<Item> items) {
        this.setItens(items);
        return this;
    }

    public Emprestimo addItens(Item item) {
        this.itens.add(item);
        item.setEmprestimo(this);
        return this;
    }

    public Emprestimo removeItens(Item item) {
        this.itens.remove(item);
        item.setEmprestimo(null);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Emprestimo usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Emprestimo)) {
            return false;
        }
        return getId() != null && getId().equals(((Emprestimo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Emprestimo{" +
            "id=" + getId() +
            ", dataEmprestimo='" + getDataEmprestimo() + "'" +
            ", dataPrevistaDevolucao='" + getDataPrevistaDevolucao() + "'" +
            ", dataDevolucao='" + getDataDevolucao() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
