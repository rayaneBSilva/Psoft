package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClienteV1ListarPorIdService implements ClienteListarPorIdService{

    @Autowired
    Validation validation;
    @Override
    public Cliente listarPorId(Long id) {
        Cliente cliente = validation.validaCliente(id, null);
        return cliente;
    }
}
