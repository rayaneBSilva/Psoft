package br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouroRepository.LogradouroRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.interfaces.LogradouroBuscarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogradouroV1BuscarService implements LogradouroBuscarService {
    @Autowired
    LogradouroRepository logradouroRepository;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public List<Logradouro> buscar() {
        return logradouroRepository.buscar();
    }
}

