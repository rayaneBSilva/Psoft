package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.V1Service;


import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPatchEmailDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoaRepository.PessoaRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces.PessoaBuscarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;




@Service
public class PessoaV1BuscaService implements PessoaBuscarService {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Pessoa> buscar() {

        return pessoaRepository.buscarPessoas();
    }

}