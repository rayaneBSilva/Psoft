package br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.repository.logradouroRepository.LogradouroRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.interfaces.LogradouroExcluirService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogradouroV1ExcluirService implements LogradouroExcluirService {
    @Autowired
    LogradouroRepository logradouroRepository;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public void excluir(Long id) {
        logradouroRepository.excluir(id);
    }
}

