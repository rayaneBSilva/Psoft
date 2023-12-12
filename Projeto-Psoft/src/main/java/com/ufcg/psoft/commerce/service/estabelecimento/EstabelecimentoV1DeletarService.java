package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoV1DeletarService implements EstabelecimentoDeletarService {

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    Validation validation;

    @Override
    public void excluir(String codigoDeAcesso, Long id) {
        Estabelecimento estabelecimento = validation.validaEstabelecimento(id, codigoDeAcesso);

        estabelecimento.getSabores().clear();
        estabelecimentoRepository.delete(estabelecimento);
    }
}
