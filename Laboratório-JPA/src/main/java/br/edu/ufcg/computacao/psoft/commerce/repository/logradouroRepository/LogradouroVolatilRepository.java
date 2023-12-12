package br.edu.ufcg.computacao.psoft.commerce.repository.logradouroRepository;


import br.edu.ufcg.computacao.psoft.commerce.exception.LogradouroNaoExisteException;
import br.edu.ufcg.computacao.psoft.commerce.model.Logradouro;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LogradouroVolatilRepository implements  LogradouroRepository {
    Map<Long, Logradouro> logradouros;
    Long nextId;
    Logradouro logradouro;

    public LogradouroVolatilRepository() {
        logradouros = new HashMap<>();
        nextId = 1L;
    }

    @Override
    public Logradouro add(Logradouro logradouro) {
        logradouro.setId(nextId);
        logradouros.put(nextId, logradouro);
        nextId++;
        return logradouro;
    }

    @Override
    public Logradouro update(Long id, Logradouro logradouro) {
        logradouro.setId(id);
        logradouros.put(id, logradouro);
        return logradouro;
    }

    @Override
    public Logradouro updateTipoNome(Long id, Logradouro logradouro) {
        Logradouro logradouroExistente = logradouros.get(id);
        logradouro.setId(id);
        logradouro.setBairro(logradouroExistente.getBairro());
        logradouro.setCidade(logradouroExistente.getCidade());
        logradouro.setEstado(logradouroExistente.getEstado());
        logradouro.setPais(logradouroExistente.getPais());
        logradouro.setCep(logradouroExistente.getCep());
        logradouro.setComplemento(logradouroExistente.getComplemento());
        logradouros.put(id, logradouro);
        return logradouro;
    }

    @Override
    public void excluir(Long id) {
        Logradouro logradouro = logradouros.get(id);
        if(logradouro == null){
            throw new LogradouroNaoExisteException();
        }else {
            logradouros.remove(id);
        }
    }

    @Override
    public List<Logradouro> buscar() {
        return new ArrayList<>(logradouros.values());
    }

    @Override
    public Logradouro buscarPorId(Long id)
    {
        Logradouro logradouro = logradouros.get(id);
        if(logradouro == null){
            throw new LogradouroNaoExisteException();
        }
        return logradouro;
    }


}