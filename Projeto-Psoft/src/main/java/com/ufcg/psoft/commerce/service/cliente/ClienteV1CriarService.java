package com.ufcg.psoft.commerce.service.cliente;


import com.ufcg.psoft.commerce.dto.cliente.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteV1CriarService implements ClienteCriarService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente criar(ClientePostPutRequestDTO clientePostPutRequestDTO) {
        Cliente cliente = modelMapper.map(clientePostPutRequestDTO, Cliente.class);
        return clienteRepository.save(cliente);
    }

}