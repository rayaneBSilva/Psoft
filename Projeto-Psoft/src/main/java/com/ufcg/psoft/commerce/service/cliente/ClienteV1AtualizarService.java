package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.dto.cliente.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteV1AtualizarService implements ClienteAtualizarService{


    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    Validation validation;

    @Override
    public Cliente atualizar(Long id, String codigoAcesso, ClientePostPutRequestDTO clientePostPutRequestDTO) {
        Cliente cliente = validation.validaCliente(id, codigoAcesso);

        cliente.setCodigoAcesso(clientePostPutRequestDTO.getCodigoAcesso());
        cliente.setEndereco(clientePostPutRequestDTO.getEndereco());
        cliente.setNome(clientePostPutRequestDTO.getNome());

        return clienteRepository.save(cliente);
    }
}