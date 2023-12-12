package br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO.LogradouroPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouroRepository.LogradouroRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.interfaces.LogradouroCriarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogradouroV1CriarService implements LogradouroCriarService {
    @Autowired
    LogradouroRepository logradouroRepository;
    @Override
    public Logradouro criar(LogradouroPostPutDTO logradouroPostPutDTO) {
        return logradouroRepository.add(
                Logradouro.builder()
                        .tipoLogradouro(logradouroPostPutDTO.getTipoLogradouro())
                        .nomeLogradouro(logradouroPostPutDTO.getNomeLogradouro())
                        .bairro((logradouroPostPutDTO.getBairro()))
                        .cidade(logradouroPostPutDTO.getCidade())
                        .estado(logradouroPostPutDTO.getEstado())
                        .pais(logradouroPostPutDTO.getPais())
                        .cep(logradouroPostPutDTO.getCep())
                        .complemento(logradouroPostPutDTO.getComplemento())
                        .build()
        );
    }
}