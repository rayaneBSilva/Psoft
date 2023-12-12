package br.edu.ufcg.computacao.psoft.commerce.repository.pessoaRepository;




import br.edu.ufcg.computacao.psoft.commerce.exception.PessoaNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import br.edu.ufcg.computacao.psoft.commerce.repository.pessoaRepository.PessoaRepository;
import org.springframework.stereotype.Repository;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Repository
public class PessoaVolatilRepository implements PessoaRepository {

    Map<Long, Pessoa> pessoas;
    Long nextId;
    Pessoa pessoa;


    public PessoaVolatilRepository() {
        pessoas = new HashMap<>();
        nextId = 1L;
    }


    @Override
    public Pessoa add(Pessoa pessoa) {
        pessoa.setId(nextId);
        pessoas.put(nextId,pessoa);
        nextId++;
        return pessoa;
    }


    @Override
    public void excluir(Long id){
        Pessoa pessoa = pessoas.get(id);
        if(pessoa == null) {
            throw new PessoaNaoExisteException();
        } else {
            pessoas.remove(id);
        }


    }
    @Override
    public Pessoa update(Long id, Pessoa pessoa) {
        Pessoa pessoaExistente = pessoas.get(id);
        pessoa.setId(id);
        pessoa.setNome(pessoaExistente.getNome());
        pessoa.setCpf(pessoaExistente.getCpf());
        pessoas.put(id, pessoa);
        return pessoa;

    }

    @Override
    public Pessoa updateEmail(Long id, Pessoa pessoa) {
        Pessoa pessoaExistente = pessoas.get(id);
        pessoa.setId(id);
        pessoa.setNome(pessoaExistente.getNome());
        pessoa.setCpf(pessoaExistente.getCpf());
        pessoa.setListaTelefones(pessoaExistente.getListaTelefones());
        pessoa.setDataNascimento(pessoaExistente.getDataNascimento());
        pessoa.setListaEnderecos(pessoaExistente.getListaEnderecos());
        pessoa.setProfissao(pessoaExistente.getProfissao());
        pessoas.put(id, pessoa);
        return pessoa;
    }


    @Override
    public void clear(){
        pessoas = new HashMap<>();
    }


    @Override
    public List<Pessoa> buscarPessoas() {

        return new ArrayList<>(pessoas.values());
    }

    @Override
    public Pessoa buscarPessoaPorId(Long id) {
        Pessoa pessoa = pessoas.get(id);
        if (pessoa == null) {
            throw new PessoaNaoExisteException();
        }
        return pessoa;
    }

}