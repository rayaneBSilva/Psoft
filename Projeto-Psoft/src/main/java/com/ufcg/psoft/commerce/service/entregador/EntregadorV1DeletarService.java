package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorV1DeletarService implements EntregadorDeletarService{
    @Autowired
    EntregadorRepository entregadorRepository;
    @Override
    public void excluir(String codigoAcesso, Long id) {
        Entregador entregador = entregadorRepository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        if (!entregador.getCodigoAcesso().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        entregadorRepository.delete(entregador);
    }
}
