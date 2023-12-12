package com.ufcg.psoft.commerce.service.associacao;

import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.commerce.model.Associacao;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AssociacaoV1CriarService implements AssociacaoCriarService {


    @Autowired
    private AssociacaoRepository associacaoRepository;
    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    private EntregadorRepository entregadorRepository;

    @Override
    public Associacao criar(Long idEntregador, String codigoAcessoEntregador, Long idEstabelecimento) {
        Entregador entregador = entregadorRepository.findById(idEntregador).orElseThrow(() -> new EntregadorNaoExisteException());
        estabelecimentoRepository.findById(idEstabelecimento).orElseThrow(() -> new EstabelecimentoNaoExisteException());
        if (!entregador.getCodigoAcesso().equals(codigoAcessoEntregador)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        Associacao associacao = Associacao.builder()
                                .entregadorId(idEntregador)
                                .estabelecimentoId(idEstabelecimento)
                                .statusAnalise(false)
                                .build();
        return associacaoRepository.save(associacao);
    }
}