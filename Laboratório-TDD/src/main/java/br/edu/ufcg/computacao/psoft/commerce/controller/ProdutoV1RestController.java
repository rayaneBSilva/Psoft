package br.edu.ufcg.computacao.psoft.commerce.controller;


import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPatchCodigoDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPatchValorDTO;
import br.edu.ufcg.computacao.psoft.commerce.dto.produtoDTO.ProdutoPostPutDTO;
import br.edu.ufcg.computacao.psoft.commerce.exception.CommerceException;
import br.edu.ufcg.computacao.psoft.commerce.service.produtoService.interfaces.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(
        value = "/v1/produto", produces = MediaType.APPLICATION_JSON_VALUE
)
public class ProdutoV1RestController {
    @Autowired
    ProdutoCriarService produtoCriarService;

    @Autowired
    ProdutoAtualizarService produtoAtualizarService;

    @Autowired
    ProdutoAtualizarCodigoService produtoAtualizarCodigoService;

    @Autowired
    ProdutoAtualizarValorService produtoAtualizarValorService;

    @Autowired
    ProdutoExcluirService produtoExcluirService;

    @Autowired
    ProdutoBuscarService produtoBuscarService;

    @Autowired
    ProdutoBuscarPorIdService produtoBuscarPorIdService;


    @PostMapping("/create")
    ResponseEntity<?> criarProduto(
            @RequestBody ProdutoPostPutDTO produtoPostPutDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoCriarService.criar(produtoPostPutDTO));
    }

    @PutMapping("/{id}/update")
    ResponseEntity<?> atualizarProduto(
            @PathVariable("id") Long id,
            @RequestBody @Valid ProdutoPostPutDTO produtoPostPutDTO) {

      
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(produtoAtualizarService.atualizar(id, produtoPostPutDTO));


    }

    @PatchMapping("/{id}/codigo-de-barra")
    ResponseEntity<?> atualizarProduto(
            @PathVariable("id") Long id ,
            @RequestBody @Valid ProdutoPatchCodigoDTO produtoPatchCodigo
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoAtualizarCodigoService.updateCodigoBarras(id, produtoPatchCodigo));
    }

    @PatchMapping("/{id}/valor")
    ResponseEntity<?> atualizarProduto(
            @PathVariable("id") Long id ,
            @RequestBody @Valid ProdutoPatchValorDTO produtoPatchValorDTO
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoAtualizarValorService.updateValor(id, produtoPatchValorDTO));
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> excluirPessoa (
            @PathVariable("id") Long id
    ){
        produtoExcluirService.excluir(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
    @GetMapping
    ResponseEntity<?> buscarTodosProdutos() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoBuscarService.buscar());
    }

    @GetMapping("{id}")
    ResponseEntity<?> buscarProdutoPorId(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoBuscarPorIdService.buscarProdutoPorId(id));
    }

}