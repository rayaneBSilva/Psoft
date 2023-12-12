package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.dto.sabor.SaborGetRequestDTO;
import com.ufcg.psoft.commerce.exception.SaborNaoExisteException;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.service.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstabelecimentoV1BuscarCardapioService implements EstabelecimentoBuscarCardapioService{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private Validation validation;

    @Override
    public List<SaborGetRequestDTO> listar(String codigoAcessoEstabelecimento, Long idEstabelecimento) {
        Estabelecimento estabelecimento = validation.validaEstabelecimento(idEstabelecimento, codigoAcessoEstabelecimento);

        List<SaborGetRequestDTO> sabores = estabelecimento.getSabores()
                .stream()
                .map(sabor -> modelMapper.map(sabor, SaborGetRequestDTO.class))
                .collect(Collectors.toList());

        sabores.sort(Comparator.comparing(SaborGetRequestDTO::isDisponivel).reversed());
        return sabores;
    }

    @Override
    public List<SaborGetRequestDTO> listar(String codigoDeAcesso, Long id, String tipo) {
        Estabelecimento estabelecimento = validation.validaEstabelecimento(id, codigoDeAcesso);

        if(tipo != null && !(tipo.equals("doce") || tipo.equals("salgado"))){
            throw new SaborNaoExisteException();
        }

        List<SaborGetRequestDTO> saboresFiltrados = new ArrayList<>();

        for (Sabor sabor : estabelecimento.getSabores()) {
            if(sabor.getTipo().equals(tipo)){
                SaborGetRequestDTO saborDTO = modelMapper.map(sabor, SaborGetRequestDTO.class);
                saboresFiltrados.add(saborDTO);
            }
        }
        return saboresFiltrados;
    }
}
