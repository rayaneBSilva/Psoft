package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.model.Entregador;

import java.util.List;

@FunctionalInterface
public interface EntregadorListarService {
    List<Entregador> listar();
}
