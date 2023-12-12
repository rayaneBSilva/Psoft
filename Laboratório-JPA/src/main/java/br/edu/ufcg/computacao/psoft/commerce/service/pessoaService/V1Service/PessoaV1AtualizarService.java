package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.V1Service;




import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoaRepository.PessoaRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces.PessoaAtualizarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class PessoaV1AtualizarService implements PessoaAtualizarService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Pessoa atualizar(Long id, PessoaPostPutDTO pessoaPostPutDTO) {
        return pessoaRepository.update(
                id,
                Pessoa.builder()
                        .email(pessoaPostPutDTO.getEmail())
                        .telefones(pessoaPostPutDTO.getListaTelefones())
                        .dataNascimento(pessoaPostPutDTO.getDataNascimento())
                       /* .listaEnderecos(pessoaPostPutDTO.getListaEnderecos()) */
                        .profissao(pessoaPostPutDTO.getProfissao())
                        .build()
        );
    }
}