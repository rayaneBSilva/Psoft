package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.exception.SaborNaoExisteException;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaborV1DeletarService implements SaborDeletarService {

    @Autowired
    private SaborRepository saborRepository;

    @Autowired
    Validation validation;
    @Override
    public void excluir(String codigoDeAcesso, Long id, Long estabelecimentoId) {
        validation.validaEstabelecimento(estabelecimentoId, codigoDeAcesso);
        Sabor sabor = saborRepository.findById(id).orElseThrow(SaborNaoExisteException::new);
        saborRepository.delete(sabor);
    }
}
