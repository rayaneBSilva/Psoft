package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.cliente.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.service.cliente.*;
import com.ufcg.psoft.commerce.service.pedido.ClienteListarPedidoEstabelecimentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE
)
public class ClienteV1RestController {

    @Autowired
    private ClienteCriarService clienteCriarService;

    @Autowired
    private ClienteAtualizarService clienteAtualizarService;

    @Autowired
    private ClienteListarPorIdService clienteListarPorIdService;

    @Autowired
    private ClienteListarService clienteListarService;

    @Autowired
    private ClienteExcluirService clienteExcluirService;

    @Autowired
    private ClienteDemonstrarInteresseService clienteDemonstrarInteresseService;

    @Autowired
    private ClienteListarPedidoEstabelecimentoService clienteListarPedidoEstabelecimentoService;

    @PostMapping("/create")
    ResponseEntity<?> criarCliente(
            @RequestBody ClientePostPutRequestDTO clientePostPutRequestDTO
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteCriarService.criar(clientePostPutRequestDTO));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> atualizarCliente(
            @PathVariable("id") Long id,
            @RequestParam("codigoAcesso") String codigoAcesso,
            @RequestBody  @Valid ClientePostPutRequestDTO clientePostPutRequestDTO
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteAtualizarService.atualizar(id, codigoAcesso, clientePostPutRequestDTO));

    }

    @GetMapping("{id}")
    ResponseEntity<?> buscarUmCliente(
            @PathVariable("id") Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPorIdService.listarPorId(id));
    }

    @GetMapping()
    ResponseEntity<?> buscarTodosClientes(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarService.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCliente(
            @PathVariable Long id,
            @RequestParam String codigoAcesso) {
        clienteExcluirService.excluir(id, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @PutMapping("/{id}/demonstrarInteresse")
    public ResponseEntity<?> demonstrarInteresse(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestParam Long saborId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteDemonstrarInteresseService.demonstrarInteresse(id, codigoAcesso, saborId));
    }

    @GetMapping("/pedidos-cliente-estabelecimento/{clienteId}/{estabelecimentoId}")
    public ResponseEntity<?> clienteListarTodosPedidosEstabelecimento(
            @PathVariable Long clienteId,
            @PathVariable Long estabelecimentoId,
            @RequestParam String clienteCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPedidoEstabelecimentoService.listar(null, clienteId, estabelecimentoId, clienteCodigoAcesso, null));
    }

    @GetMapping("/pedidos-cliente-estabelecimento/{clienteId}/{estabelecimentoId}/{status}")
    public ResponseEntity<?> clienteListarTodosPedidosEstabelecimento(
            @PathVariable Long clienteId,
            @PathVariable Long estabelecimentoId,
            @PathVariable String status,
            @RequestParam String clienteCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPedidoEstabelecimentoService.listar(null, clienteId, estabelecimentoId, clienteCodigoAcesso, status));
    }

    @GetMapping("/pedido-cliente-estabelecimento/{clienteId}/{estabelecimentoId}/{pedidoId}")
    public ResponseEntity<?> clienteListarPedidoEstabelecimento(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId,
            @PathVariable Long estabelecimentoId,
            @RequestParam String clienteCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPedidoEstabelecimentoService.listar(pedidoId, clienteId, estabelecimentoId, clienteCodigoAcesso, null));
    }

}