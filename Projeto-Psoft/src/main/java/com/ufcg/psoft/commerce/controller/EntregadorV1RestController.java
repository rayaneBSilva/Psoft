package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.entregador.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/entregadores")
public class EntregadorV1RestController {

    @Autowired
    EntregadorCriarService entregadorCriarService;

    @Autowired
    EntregadorAtualizarService entregadorAtualizarService;

    @Autowired
    EntregadorListarService entregadorListarService;

    @Autowired
    EntregadorListarByIdService entregadorListarByIdService;

    @Autowired
    EntregadorDeletarService entregadorDeletarService;

    @Autowired
    EntregadorDefinirDisponibilidadeService entregadorDefinirDisponibilidadeService;



    @PostMapping
    ResponseEntity<?> criarEntregador(
            @RequestBody @Valid EntregadorPostPutRequestDTO entregadorPostPutRequestDTO
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entregadorCriarService.criar(entregadorPostPutRequestDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> atualizarEntregador(
            @RequestParam String codigoAcesso,
            @PathVariable("id") Long id,
            @RequestBody @Valid EntregadorPostPutRequestDTO entregadorPostPutRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorAtualizarService.atualizar(codigoAcesso, id, entregadorPostPutRequestDTO));
    }

    @GetMapping()
    public ResponseEntity<?> listarEntregadores(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorListarService.listar());
    }


    @GetMapping("{id}")
    public ResponseEntity<?> recuperarEntregador(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorListarByIdService.listarById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerEntregador(
            @RequestParam String codigoAcesso,
            @PathVariable("id") Long id
    ) {
        entregadorDeletarService.excluir(codigoAcesso, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("{id}/disponibilidade")
    public ResponseEntity<?> definirDisponibilidade(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestParam boolean disponibilidade,
            @RequestBody @Valid EntregadorPostPutRequestDTO entregadorPostPutRequestDto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorDefinirDisponibilidadeService.definirDisponibilidade(id, entregadorPostPutRequestDto, codigoAcesso, disponibilidade));
    }

}
