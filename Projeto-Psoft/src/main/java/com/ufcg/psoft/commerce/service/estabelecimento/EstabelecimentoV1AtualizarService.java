package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EstabelecimentoV1AtualizarService implements EstabelecimentoAtualizarService{

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validation validation;

    @Override
    public Estabelecimento atualizar(String codigoDeAcesso, Long id, EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO){
        Estabelecimento estabelecimentoExistente = validation.validaEstabelecimento(id, codigoDeAcesso);

        Set<Sabor> saboresExistente = estabelecimentoExistente.getSabores();
        List<Entregador> entregadoresAprovados = estabelecimentoExistente.getEntregadoresAprovados();

        modelMapper.map(estabelecimentoPostPutRequestDTO, estabelecimentoExistente);

        estabelecimentoExistente.setSabores(saboresExistente);
        estabelecimentoExistente.setEntregadoresAprovados(entregadoresAprovados);

        return estabelecimentoRepository.save(estabelecimentoExistente);
    }
}
