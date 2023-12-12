package com.ufcg.psoft.commerce.controller;


import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.pedido.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE
)
public class PedidoV1RestController {

    @Autowired
    PedidoCriarService pedidoCriarService;

    @Autowired
    PedidoAtualizarService pedidoAtualizarService;

    @Autowired
    ClienteBuscaPedidoService clienteBuscaPedidoService;

    @Autowired
    EstabelecimentoBuscaPedidoService estabelecimentoBuscaPedidoService;

    @Autowired
    ClienteExcluirPedidoService pedidoExcluirService;

    @Autowired
    EstabelecimentoExcluirPedidoService estabelecimentoExcluirPedidoService;

    @Autowired
    PedidoConfirmaPagamentoService pedidoConfirmaPagamentoService;

    @Autowired
    EstabelecimentoPrepararPedidoService estabelecimentoPrepararPedidoService;

    @Autowired
    ClienteConfirmarEntregaPedidoService clienteConfirmarEntregaPedidoService;

    @Autowired
    EstabelecimentoAssociarPedidoEntregadorService estabelecimentoAssociarPedidoEntregadorService;

    @Autowired
    ClienteCancelarPedidoService clienteCancelarPedidoService;

    @PostMapping
    ResponseEntity<?> criarPedido(
            @RequestParam("clienteId") Long clienteId,
            @RequestParam("clienteCodigoAcesso") String clienteCodigoAcesso,
            @RequestParam("estabelecimentoId") Long estabelecimentoId,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoCriarService.criar(clienteId, clienteCodigoAcesso, estabelecimentoId, pedidoPostPutRequestDTO));

    }

    @PutMapping
    ResponseEntity<?> atualizarPedido(
            @RequestParam("pedidoId") Long pedidoId,
            @RequestParam("codigoAcesso") String codigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoAtualizarService.atualizar(pedidoId, codigoAcesso, pedidoPostPutRequestDTO));
    }

    @PutMapping("/{clienteId}/confirmar-pagamento")
    public ResponseEntity<?> confirmarPagamento(
            @PathVariable Long clienteId,
            @RequestParam String codigoAcessoCliente,
            @RequestParam Long pedidoId,
            @RequestParam String metodoPagamento,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDto) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoConfirmaPagamentoService.confirmarPagamento(clienteId, pedidoId, metodoPagamento, codigoAcessoCliente, pedidoPostPutRequestDto));
    }

    @GetMapping("/{pedidoId}/{clienteId}")
    ResponseEntity<?> clienteBuscaUmPedido(
            @PathVariable("pedidoId") Long pedidoId,
            @PathVariable("clienteId") Long clienteId,
            @RequestParam("codigoAcesso") String codigoAcesso
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteBuscaPedidoService.clienteBusca(pedidoId, clienteId, codigoAcesso));
    }

    @GetMapping
    ResponseEntity<?> clienteBuscaTodosOsPedidos (
            @RequestParam("idCliente") Long clienteId,
            @RequestParam("codigoAcesso") String codigoAcesso){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteBuscaPedidoService.clienteBusca(clienteId, codigoAcesso));
    }

    @GetMapping("/{pedidoId}/{estabelecimentoId}/{estabelecimentoCodigoAcesso}")
    ResponseEntity<?> estabelecimentoBuscaUmPedido(
            @PathVariable("pedidoId") Long pedidoId,
            @PathVariable("estabelecimentoId") Long estabelecimentoId,
            @RequestParam("estabelecimentoCodigoAcesso") String estabelecimentoCodigoAcesso
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoBuscaPedidoService.estabelecimentoBusca(pedidoId, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @GetMapping("buscar-pedidos/{estabelecimentoId}/{estabelecimentoCodigoAcesso}")
    public ResponseEntity<?> estabelecimentoBuscarTodosOsPedidos(
            @RequestParam("idEstabelecimento") Long estabelecimentoId,
            @RequestParam("codigoAcesso") String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoBuscaPedidoService.estabelecimentoBusca(estabelecimentoId, codigoAcesso));
    }

    @DeleteMapping("/{pedidoId}/{clienteId}")
    public ResponseEntity<?> clienteExcluirPedido(
            @PathVariable("pedidoId") Long pedidoId,
            @PathVariable("clienteId") Long clienteId,
            @RequestParam("codigoAcesso") String codigoAcesso
    ) {
        pedidoExcluirService.excluir(pedidoId, clienteId, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
    @DeleteMapping("/excluir-pedido/{clienteId}")
    public ResponseEntity<?> clienteExcluirTodosOsPedidos (
        @PathVariable("clienteId") Long clienteId,
        @RequestParam("codigoAcesso")  String codigoAcesso
    ){
        pedidoExcluirService.excluir(clienteId, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{pedidoId}/{estabelecimentoId}/{estabelecimentoCodigoAcesso}")
    public ResponseEntity<?> estabelecimentoExcluirPedido(
            @PathVariable("pedidoId") Long pedidoId,
            @PathVariable("estabelecimentoId") Long estabelecimentoId,
            @RequestParam("codigoAcesso") String codigoAcesso
    ) {
        estabelecimentoExcluirPedidoService.excluir(pedidoId, estabelecimentoId, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();

    }
    @DeleteMapping("/excluir-pedido-estabelecimento/{estabelecimentoId}")
    public ResponseEntity<?> estabelecimentoExcluirTodosOsPedidos (
            @PathVariable("estabelecimentoId") Long estabelecimentoId,
            @RequestParam("codigoAcesso")  String codigoAcesso
    ){
        estabelecimentoExcluirPedidoService.excluir(estabelecimentoId, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{pedidoId}/cancelar-pedido")
    public ResponseEntity<?> cancelarPedido(
            @PathVariable Long pedidoId,
            @RequestParam String clienteCodigoAcesso) {
        clienteCancelarPedidoService.cancelar(pedidoId, clienteCodigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{pedidoId}/preparar-pedido")
    public ResponseEntity<?> estabelecimentoPrepararPedido(
            @PathVariable Long pedidoId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoPrepararPedidoService.prepararPedido(pedidoId, estabelecimentoId, estabelecimentoCodigoAcesso, pedidoPostPutRequestDTO));
    }

    @PutMapping("/{pedidoId}/{clienteId}/cliente-confirmar-entrega")
    public ResponseEntity<?> clienteConfirmaEntrega(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId,
            @RequestParam String clienteCodigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteConfirmarEntregaPedidoService.clienteConfirmaEntrega(pedidoId, clienteId, clienteCodigoAcesso, pedidoPostPutRequestDTO));
    }

    @PutMapping("/{pedidoId}/associar-pedido-entregador")
    public ResponseEntity<?> estabelecimentoAssociaPedidoEntregador(
            @PathVariable Long pedidoId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoAssociarPedidoEntregadorService.associarPedidoEntregador(pedidoId, estabelecimentoId, estabelecimentoCodigoAcesso, pedidoPostPutRequestDTO));
    }
}