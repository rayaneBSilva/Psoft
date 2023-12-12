package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.sabor.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.sabor.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/sabores", produces = MediaType.APPLICATION_JSON_VALUE)
public class SaborV1RestController {

    @Autowired
    SaborCriarService saborCriarService;

    @Autowired
    SaborAtualizarService saborAtualizarService;

    @Autowired
    SaborListarService saborListarService;

    @Autowired
    SaborDeletarService saborDeletarService;

    @Autowired
    SaborAlterarDisponibilidadeService saborAlterarDisponibilidadeService;

    @PostMapping
    ResponseEntity<?> criarSabor( @RequestBody @Valid SaborPostPutRequestDTO saborPostPutRequestDto,
                                  @RequestParam Long estabelecimentoId,
                                  @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saborCriarService.criar(saborPostPutRequestDto, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @PutMapping()
    ResponseEntity<?> atualizarSabor(
            @RequestParam Long saborId,
            @RequestBody @Valid SaborPostPutRequestDTO saborPostPutRequestDto,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(saborAtualizarService.atualizar(saborId, saborPostPutRequestDto, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @GetMapping()
    public ResponseEntity<?> listarSabores(
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(saborListarService.listar(null, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @GetMapping("{saborId}")
    public ResponseEntity<?> listarUmSabor(
            @PathVariable Long saborId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(saborListarService.listar(saborId, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @DeleteMapping()
    public ResponseEntity<?> excluirSabor(
            @RequestParam Long saborId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        saborDeletarService.excluir(estabelecimentoCodigoAcesso, saborId, estabelecimentoId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @PutMapping("{saborId}/{disponibilidade}")
    public ResponseEntity<?> atualizarDisponibilidadeSabor(
            @PathVariable Long saborId,
            @PathVariable boolean disponibilidade,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(saborAlterarDisponibilidadeService.alterarDisponibilidade(saborId, estabelecimentoId, estabelecimentoCodigoAcesso, disponibilidade));
    }
}
