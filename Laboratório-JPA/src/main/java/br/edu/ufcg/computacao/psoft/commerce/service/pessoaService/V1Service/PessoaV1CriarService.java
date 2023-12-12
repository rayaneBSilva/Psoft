package br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.V1Service;
import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoaRepository.PessoaRepository;
import br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces.PessoaCriarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PessoaV1CriarService implements PessoaCriarService {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Override
    public Pessoa criar(PessoaPostPutDTO pessoaPostPutDTO) {
        return pessoaRepository.add(
                Pessoa.builder()
                        .nome(pessoaPostPutDTO.getNome())
                        .cpf(pessoaPostPutDTO.getCpf())
                        .email(pessoaPostPutDTO.getEmail())
                        .listaTelefones(pessoaPostPutDTO.getListaTelefones())
                        .dataNascimento(pessoaPostPutDTO.getDataNascimento())
                        /*.listaEnderecos(pessoaPostPutDTO.getListaEnderecos()) */
                        .profissao(pessoaPostPutDTO.getProfissao())
                        .build());
    }
}