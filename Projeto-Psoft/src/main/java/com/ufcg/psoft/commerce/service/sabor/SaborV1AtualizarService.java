package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.dto.sabor.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.SaborNaoExisteException;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaborV1AtualizarService implements SaborAtualizarService {

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Validation validation;
    @Override
    public Sabor atualizar(Long saborId, SaborPostPutRequestDTO saborPostPutRequestDTO, Long estabelecimentoId, String estabelecimentoCodigoAcesso) {
        validation.validaEstabelecimento(estabelecimentoId, estabelecimentoCodigoAcesso);

        Sabor sabor = saborRepository.findById(saborId).orElseThrow(SaborNaoExisteException::new);

        modelMapper.map(saborPostPutRequestDTO, sabor);
        return saborRepository.save(sabor);
    }
}
