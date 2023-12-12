package com.ufcg.psoft.commerce.repository;

import com.ufcg.psoft.commerce.model.Associacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociacaoRepository extends JpaRepository<Associacao, Long>  {
    Associacao findByEstabelecimentoIdAndEntregadorId(Long idEstabelecimento, Long idEntregador);
}
