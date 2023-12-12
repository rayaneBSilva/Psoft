package com.ufcg.psoft.commerce.service.associacao;

import com.ufcg.psoft.commerce.model.Associacao;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.service.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AssociacaoV1AprovarService implements AssociacaoAprovarService {

    @Autowired
    private AssociacaoRepository associacaoRepository;
    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    private EntregadorRepository entregadorRepository;

    @Autowired
    private Validation validation;

    @Override
    public Associacao atualizar(String codigoAcesso, Long idEstabelecimento, Long idEntregador) {
        Estabelecimento estabelecimento = validation.validaEstabelecimento(idEstabelecimento, codigoAcesso);
        Entregador entregador = validation.validaEntregador(idEntregador, null);

        Associacao associacao = associacaoRepository.findByEstabelecimentoIdAndEntregadorId(idEstabelecimento, idEntregador);
        associacao.setStatusAnalise(true);

        List<Entregador> entregadoresAprovados = estabelecimento.getEntregadoresAprovados();
        entregadoresAprovados.add(entregador);
        estabelecimento.setEntregadoresAprovados(entregadoresAprovados);
        estabelecimentoRepository.save(estabelecimento);
        entregadorRepository.save(entregador);

        return associacaoRepository.save(associacao);
    }
}
