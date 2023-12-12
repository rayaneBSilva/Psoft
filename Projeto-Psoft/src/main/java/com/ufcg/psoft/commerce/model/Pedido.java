package com.ufcg.psoft.commerce.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "pedidos")
@Table(name = "pedidos")
public class Pedido {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("enderecoEntrega")
    @Column
    private String enderecoEntrega;

    @JsonProperty("preco")
    @Column(nullable = false)
    private double preco;

    @JsonProperty("clienteId")
    @Column(nullable = false)
    private Long clienteId;

    @JsonProperty("estabelecimentoId")
    @Column(nullable = false)
    private Long estabelecimentoId;

    @JsonProperty("entregadorId")
    @Column(nullable = true)
    private Long entregadorId;

    @JsonProperty("statusPagamento")
    @Column(nullable = false)
    @Builder.Default
    private Boolean statusPagamento = false;

    @JsonProperty("statusEntrega")
    @Column
    @Builder.Default
    private String statusEntrega = "Pedido recebido";

    @JsonProperty
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "pizzas")
    private List<Pizza> pizzas = new ArrayList<>();
}
