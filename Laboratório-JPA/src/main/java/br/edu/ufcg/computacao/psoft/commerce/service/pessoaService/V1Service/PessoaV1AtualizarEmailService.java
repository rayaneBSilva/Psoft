package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.V1Service;

import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPatchEmailDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoaRepository.PessoaRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces.PessoaAtualizarEmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PessoaV1AtualizarEmailService implements PessoaAtualizarEmailService {


    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Pessoa updateEmail(Long id, PessoaPatchEmailDTO pessoaPatchEmailDTO) {
        return
                pessoaRepository.updateEmail(
                        id,
                        modelMapper.map(pessoaPatchEmailDTO, Pessoa.class)
                );
    }
}
