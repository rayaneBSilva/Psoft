package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClienteV1ExcluirService implements ClienteExcluirService{

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    Validation validation;

    @Override
    public void excluir(Long id, String codigoAcesso) {
        Cliente cliente = validation.validaCliente(id, codigoAcesso);
        clienteRepository.delete(cliente);
    }
}
