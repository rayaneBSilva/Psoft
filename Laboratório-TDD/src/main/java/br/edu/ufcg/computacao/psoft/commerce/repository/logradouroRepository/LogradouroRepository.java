package br.edu.ufcg.computacao.psoft.commerce.repository.logradouroRepository;

import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;

import java.util.List;

public interface LogradouroRepository {
    public Logradouro add(Logradouro logradouro);
    public Logradouro update(Long id, Logradouro logradouro);
    public Logradouro updateTipoNome(Long id, Logradouro logradouro);
    public void excluir(Long id);
    public List<Logradouro> buscar();

    public Logradouro buscarPorId(Long id);
}