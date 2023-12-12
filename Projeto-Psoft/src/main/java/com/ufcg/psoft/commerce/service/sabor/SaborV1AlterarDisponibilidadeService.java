package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaborV1AlterarDisponibilidadeService implements SaborAlterarDisponibilidadeService {

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    private Validation validation;


    @Override
    public Sabor alterarDisponibilidade(Long saborId, Long estabelecimentoId, String estabelecimentoCodigoAcesso, Boolean disponibilidade) {
        validation.validaEstabelecimento(estabelecimentoId, estabelecimentoCodigoAcesso);

        Sabor sabor = saborRepository.findById(saborId).orElseThrow(SaborNaoExisteException::new);
        if (disponibilidade) {
            if (sabor.isDisponivel()) {
                throw new SaborJaDisponivelException();
            }
        } else {
            if (!sabor.isDisponivel()) {
                throw new SaborJaIndisponivelException();
            }
        }
        sabor.setDisponivel(disponibilidade);
        return saborRepository.save(sabor);
    }

}
