package com.ufcg.psoft.commerce.service.sabor;


import com.ufcg.psoft.commerce.exception.SaborNaoExisteException;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaborV1ListarService implements SaborListarService{

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    Validation validation;
    @Override
    public List<Sabor> listar(Long saborId, Long estabelecimentoId, String codigoDeAcesso) {
        validation.validaEstabelecimento(estabelecimentoId, codigoDeAcesso);

        if (saborId != null && saborId > 0) {
            return List.of(saborRepository.findById(saborId).orElseThrow(SaborNaoExisteException::new));
        }
        return saborRepository.findAll();
    }

}
