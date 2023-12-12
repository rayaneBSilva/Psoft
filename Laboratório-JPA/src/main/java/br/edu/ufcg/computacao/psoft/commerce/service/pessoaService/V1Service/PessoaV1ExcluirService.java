package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.repository.pessoaRepository.PessoaRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces.PessoaExcluirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PessoaV1ExcluirService implements PessoaExcluirService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Override
    public void excluir(Long id) {
        pessoaRepository.excluir(id);
    }
}