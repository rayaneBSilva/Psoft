package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "estabelecimentos")
@Table(name = "estabelecimentos")
public class Estabelecimento {

    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pk_id_estabelecimento")
    private Long id;

    @JsonProperty("codigoAcesso")
    @Pattern(regexp = "\\d{6}", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    @NotBlank(message = "Codigo de acesso Obrigatorio!")
    @Column(name = "desc_codigoAcesso", nullable = false)
    private String codigoAcesso;

    @JsonProperty("sabores")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JoinColumn(name = "sabores")
    private Set<Sabor> sabores = new HashSet<>();

    @JsonProperty("entregadores")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JoinColumn(name = "entregadores")
    private List<Entregador> entregadoresAprovados = new LinkedList<>();

    @JsonProperty("pedidos")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JoinColumn(name = "pedidos")
    private List<Pedido> pedidosPendentes = new LinkedList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estabelecimento that = (Estabelecimento) o;
        return Objects.equals(id, that.id) && Objects.equals(codigoAcesso, that.codigoAcesso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigoAcesso);
    }

}
