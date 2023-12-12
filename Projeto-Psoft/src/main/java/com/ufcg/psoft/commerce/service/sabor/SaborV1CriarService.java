package com.ufcg.psoft.commerce.service.sabor;

import com.ufcg.psoft.commerce.dto.sabor.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SaborV1CriarService implements SaborCriarService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    Validation validation;

    @Override
    public Sabor criar(SaborPostPutRequestDTO saborPostPutRequestDTO, Long estabelecimentoId, String codigoDeAcesso) {
        Estabelecimento estabelecimento = validation.validaEstabelecimento(estabelecimentoId, codigoDeAcesso);

        Sabor sabor = modelMapper.map(saborPostPutRequestDTO, Sabor.class);
        Sabor resultado = saborRepository.save(sabor);

        Set<Sabor> sabores = estabelecimento.getSabores();

        sabores.add(sabor);
        estabelecimento.setSabores(sabores);
        estabelecimentoRepository.save(estabelecimento);

        return resultado;
    }
}
