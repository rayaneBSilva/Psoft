package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.dto.cliente.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Cliente;

import java.util.List;

@FunctionalInterface
public interface ClienteListarService {
    List<Cliente> listar();
}