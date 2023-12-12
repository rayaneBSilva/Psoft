package br.edu.ufcg.computacao.psoft.commerce.controller;

import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPatchEmailDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.pessoaDTO.PessoaPostPutDTO;


import br.edu.ufcg.computacao.psoft.commerce.service.pessoaService.interfaces.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping(
        value = "/v1/pessoa" , produces = MediaType.APPLICATION_JSON_VALUE
)
public class PessoaV1RestController {


    @Autowired
    PessoaCriarService pessoaCriarService;
    @Autowired
    PessoaAtualizarService pessoaAtualizarService;
    @Autowired
    PessoaAtualizarEmailService pessoaAtualizarEmailService;
    @Autowired
    PessoaExcluirService pessoaExcluirService;
    @Autowired
    PessoaBuscarService pessoaBuscarService;
    @Autowired
    PessoaBuscarPorIdService pessoaBuscarPorIdService;




    @PostMapping("/create")
    ResponseEntity<?> criarPessoa(
            @RequestBody PessoaPostPutDTO pessoaPostPutDTO
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pessoaCriarService.criar(pessoaPostPutDTO));
    }




    @PutMapping("/{id}/update")
    ResponseEntity<?> atualizarPessoa(
            @PathVariable("id") Long id,
            @RequestBody @Valid PessoaPostPutDTO pessoaPostPutDTO
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pessoaAtualizarService.atualizar(id, pessoaPostPutDTO));
    }


    @PatchMapping("/{id}/email")
    ResponseEntity<?> atualizarPessoa(
            @PathVariable("id") Long id ,
            @RequestBody @Valid PessoaPatchEmailDTO pessoaPatchEmailDTO
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pessoaAtualizarEmailService.updateEmail(id, pessoaPatchEmailDTO));
    }


    @DeleteMapping("{id}")
    ResponseEntity<?> excluirPessoa (
            @PathVariable("id") Long id
    ){
        pessoaExcluirService.excluir(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping
    ResponseEntity<?> buscarTodasPessoas() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pessoaBuscarService.buscar());
    }

    @GetMapping("{id}")
    ResponseEntity<?> buscarPessoaPorId(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pessoaBuscarPorIdService.buscarPessoaPorId(id));
    }

}