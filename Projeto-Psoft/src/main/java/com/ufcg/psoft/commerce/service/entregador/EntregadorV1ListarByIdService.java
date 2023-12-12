package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.IdNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorV1ListarByIdService implements EntregadorListarByIdService{
    @Autowired
    EntregadorRepository entregadorRepository;
    @Override
    public Entregador listarById(Long id) {
        if(id >= 0){
            return entregadorRepository.findById(id).orElseThrow(() -> new EntregadorNaoExisteException());
        }
        else{
            throw new IdNaoExisteException();
        }
    }
}
