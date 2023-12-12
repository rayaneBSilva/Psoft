package br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO.LogradouroPatchTipoNomeDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouroRepository.LogradouroRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.interfaces.LogradouroAtualizarTipoNomeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogradouroV1AtualizarTipoNomeService implements LogradouroAtualizarTipoNomeService {
    @Autowired
    LogradouroRepository logradouroRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Logradouro atualizarNomeTipo(Long id, LogradouroPatchTipoNomeDTO logradouroPatchTipoNomeDTO) {
        return logradouroRepository.updateTipoNome(
                id,
                modelMapper.map(logradouroPatchTipoNomeDTO, Logradouro.class)
        );
    }
}
