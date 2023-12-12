package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoV1CriarService implements EstabelecimentoCriarService{

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Estabelecimento criar(EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO) {
        Estabelecimento estabelecimento = modelMapper.map(estabelecimentoPostPutRequestDTO, Estabelecimento.class);
        return estabelecimentoRepository.save(estabelecimento);
    }
}
