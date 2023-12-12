package com.ufcg.psoft.commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ufcg.psoft.commerce.service.associacao.*;

@RestController
@RequestMapping(
        value = "/associacao", produces = MediaType.APPLICATION_JSON_VALUE
)
public class AssociacaoV1RestController {

    @Autowired
    AssociacaoCriarService associacaoCriarService;

    @Autowired
    AssociacaoAprovarService associacaoAprovarService;

    @PostMapping
    ResponseEntity<?> criarAssociacao(@RequestParam Long entregadorId,
                                      @RequestParam String codigoAcesso,
                                      @RequestParam Long estabelecimentoId
                                      ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(associacaoCriarService.criar(entregadorId, codigoAcesso, estabelecimentoId));
    }

    @PutMapping
    ResponseEntity<?> aprovarAssociacao(
            @RequestParam String codigoAcesso,
            @RequestParam Long estabelecimentoId,
            @RequestParam Long entregadorId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(associacaoAprovarService.atualizar(codigoAcesso, estabelecimentoId, entregadorId));
    }
}
