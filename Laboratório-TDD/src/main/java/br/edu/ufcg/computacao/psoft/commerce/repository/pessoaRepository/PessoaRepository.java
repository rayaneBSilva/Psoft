package br.edu.ufcg.computacao.psoft.commerce.repository.pessoaRepository;




import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;


import java.util.List;


public interface PessoaRepository {
    public Pessoa add(Pessoa pessoa);


    public Pessoa update(Long id, Pessoa pessoa);

    public Pessoa updateEmail(Long id, Pessoa pessoa);
    public void excluir(Long id);
    public void clear();
    public List<Pessoa> buscarPessoas();
    public Pessoa buscarPessoaPorId(Long id);
}