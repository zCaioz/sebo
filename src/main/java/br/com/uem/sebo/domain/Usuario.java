package br.com.uem.sebo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
    @JsonIgnoreProperties(value = { "usuario", "itens" }, allowSetters = true)
    private Set<Emprestimo> emprestimos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
    @JsonIgnoreProperties(value = { "usuario", "itens" }, allowSetters = true)
    private Set<Venda> vendas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Usuario nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }

    public Usuario email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Usuario telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Set<Emprestimo> getEmprestimos() {
        return this.emprestimos;
    }

    public void setEmprestimos(Set<Emprestimo> emprestimos) {
        if (this.emprestimos != null) {
            this.emprestimos.forEach(i -> i.setUsuario(null));
        }
        if (emprestimos != null) {
            emprestimos.forEach(i -> i.setUsuario(this));
        }
        this.emprestimos = emprestimos;
    }

    public Usuario emprestimos(Set<Emprestimo> emprestimos) {
        this.setEmprestimos(emprestimos);
        return this;
    }

    public Usuario addEmprestimos(Emprestimo emprestimo) {
        this.emprestimos.add(emprestimo);
        emprestimo.setUsuario(this);
        return this;
    }

    public Usuario removeEmprestimos(Emprestimo emprestimo) {
        this.emprestimos.remove(emprestimo);
        emprestimo.setUsuario(null);
        return this;
    }

    public Set<Venda> getVendas() {
        return this.vendas;
    }

    public void setVendas(Set<Venda> vendas) {
        if (this.vendas != null) {
            this.vendas.forEach(i -> i.setUsuario(null));
        }
        if (vendas != null) {
            vendas.forEach(i -> i.setUsuario(this));
        }
        this.vendas = vendas;
    }

    public Usuario vendas(Set<Venda> vendas) {
        this.setVendas(vendas);
        return this;
    }

    public Usuario addVendas(Venda venda) {
        this.vendas.add(venda);
        venda.setUsuario(this);
        return this;
    }

    public Usuario removeVendas(Venda venda) {
        this.vendas.remove(venda);
        venda.setUsuario(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return getId() != null && getId().equals(((Usuario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefone='" + getTelefone() + "'" +
            "}";
    }
}
