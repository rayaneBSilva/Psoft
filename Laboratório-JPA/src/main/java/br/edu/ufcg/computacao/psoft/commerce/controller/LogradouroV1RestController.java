package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO.LogradouroPatchTipoNomeDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.logradouroDTO.LogradouroPostPutDTO;


import br.edu.ufcg.computacao.psoft.commerce.service.logradouroService.interfaces.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "v1/logradouro" , produces = MediaType.APPLICATION_JSON_VALUE
)
public class LogradouroV1RestController {

  @Autowired
  LogradouroAtualizarTipoNomeService logradouroAtualizarTipoNomeService;

   @Autowired
   LogradouroCriarService logradouroCriarService;

   @Autowired
   LogradouroAtualizarService logradouroAtualizarService;

    @Autowired
    LogradouroExcluirService logradouroExcluirService;

    @Autowired
    LogradouroBuscarService logradouroBuscarService;

    @Autowired
    LogradouroBuscarPorIdService logradouroBuscarPorIdService;

    @PostMapping("/create")
    ResponseEntity<?> criarLogradouro(
            @RequestBody LogradouroPostPutDTO logradouroPostPutDTO
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(logradouroCriarService.criar(logradouroPostPutDTO));
    }

    @PutMapping("/{id}/update")
    ResponseEntity<?> atualizarLogradouro(
            @PathVariable("id") Long id,
            @RequestBody @Valid LogradouroPostPutDTO logradouroPostPutDTO
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logradouroAtualizarService.atualizar(id, logradouroPostPutDTO));
    }

    @PatchMapping("/{id}/update")
    ResponseEntity<?> atualizarTipoNomeLogradouro(
            @PathVariable("id") Long id,
            @RequestBody @Valid LogradouroPatchTipoNomeDTO  logradouroPatchTipoNomeDTO
    ){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logradouroAtualizarTipoNomeService.atualizarNomeTipo(id, logradouroPatchTipoNomeDTO));
    }

    @DeleteMapping("/{id}/delete")
    ResponseEntity<?> excluirLogradouro(
            @PathVariable Long id
    ){
        logradouroExcluirService.excluir(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping
    ResponseEntity<?> buscarTodosLogradouros(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logradouroBuscarService.buscar());
    }

    @GetMapping("{id}")
    ResponseEntity<?> buscarLogradouroPorId(
            @PathVariable("id") Long id
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logradouroBuscarPorIdService.buscarLogradouroPorId(id));
    }

}