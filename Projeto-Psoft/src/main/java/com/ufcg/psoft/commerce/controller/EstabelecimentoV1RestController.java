package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.estabelecimento.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/estabelecimentos", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstabelecimentoV1RestController {

    @Autowired
    EstabelecimentoCriarService estabelecimentoCriarService;

    @Autowired
    EstabelecimentoAtualizarService estabelecimentoAtualizarService;

    @Autowired
    EstabelecimentoListarService estabelecimentoListarService;

    @Autowired
    EstabelecimentoDeletarService estabelecimentoDeletarService;

    @Autowired
    EstabelecimentoBuscarCardapioService estabelecimentoBuscarCardapioService;


    @PostMapping
    ResponseEntity<?> criarEstabelecimento(@RequestBody @Valid EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estabelecimentoCriarService.criar(estabelecimentoPostPutRequestDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> atualizarEstabelecimento(
            @RequestParam String codigoAcesso,
            @PathVariable("id") Long id,
            @RequestBody @Valid EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoAtualizarService.atualizar(codigoAcesso, id, estabelecimentoPostPutRequestDTO));
    }

    @GetMapping()
    public ResponseEntity<?> listarEstabelecimentos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoListarService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarEstabelecimento(
            @RequestParam String codigoAcesso,
            @PathVariable("id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoListarService.listar(codigoAcesso, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerEstabelecimento(
            @RequestParam String codigoAcesso,
            @PathVariable("id") Long id
    ) {
        this.estabelecimentoDeletarService.excluir(codigoAcesso, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}/sabores")
    public ResponseEntity<?> recuperarCardapio(
            @RequestParam String codigoAcesso,
            @PathVariable("id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoBuscarCardapioService.listar(codigoAcesso, id));
    }

    @GetMapping("/{id}/sabores/tipo")
    public ResponseEntity<?> recuperarCardapioPorTipo(
            @RequestParam String codigoAcesso,
            @PathVariable("id") Long id,
            @RequestParam String tipo
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoBuscarCardapioService.listar(codigoAcesso, id, tipo));
    }
}
