package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EstabelecimentoV1ListarService implements EstabelecimentoListarService{

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public Collection<Estabelecimento> listar() {
        return estabelecimentoRepository.findAll();
    }

    @Override
    public Estabelecimento listar(String codigoDeAcesso, Long id) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id).orElseThrow(() -> new EstabelecimentoNaoExisteException());
        if (!estabelecimento.getCodigoAcesso().equals(codigoDeAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        return estabelecimento;
    }
}
