package br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;
import br.edu.ufcg.computacao.psoft.commerce.repository.logradouroRepository.LogradouroRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.interfaces.LogradouroBuscarPorIdService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogradouroV1BuscarPorIdService implements LogradouroBuscarPorIdService {
    @Autowired
    LogradouroRepository logradouroRepository;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public Logradouro buscarLogradouroPorId(Long id) {
        return logradouroRepository.buscarPorId(id);
    }

}
