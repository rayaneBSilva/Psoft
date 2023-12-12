package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoaRepository.PessoaRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces.PessoaBuscarPorIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PessoaV1BuscarPorIdService implements PessoaBuscarPorIdService {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Override
    public Pessoa buscarPessoaPorId(Long id) {
        return pessoaRepository.buscarPessoaPorId(id);
    }
}