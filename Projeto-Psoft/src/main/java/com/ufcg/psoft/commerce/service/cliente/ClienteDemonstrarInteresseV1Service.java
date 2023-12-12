package com.ufcg.psoft.commerce.service.cliente;


import com.ufcg.psoft.commerce.exception.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.SaborJaDisponivelException;
import com.ufcg.psoft.commerce.exception.SaborNaoExisteException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteDemonstrarInteresseV1Service implements ClienteDemonstrarInteresseService {


    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    SaborRepository saborRepository;

    @Override
    public Sabor demonstrarInteresse(Long clienteId, String codigoAcesso, Long saborId) {   Sabor sabor = saborRepository.findById(saborId).orElseThrow(() -> new SaborNaoExisteException());
        if (sabor.isDisponivel()) {
            throw new SaborJaDisponivelException();
        }

        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(()-> new ClienteNaoExisteException());
        if (!cliente.getCodigoAcesso().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        sabor.getClientesInteressados().add(cliente);
        return saborRepository.save(sabor);
    }
}

