package br.edu.ufcg.computacao.psoft.commerce.controller;
import br.edu.ufcg.computacao.psoft.commerce.model.Endereco;
import br.edu.ufcg.computacao.psoft.commerce.model.Pessoa;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/enderecos")
public class EnderecoV1RestController {

        private Map <Long, Endereco> enderecoRepository;

        private Long enderecoNextId;


        public EnderecoV1RestController() {
            this.enderecoRepository = new HashMap<>();
            this.enderecoNextId = 1L;

        }

        @PostMapping
        public ResponseEntity<?> createEndereco(@RequestBody Endereco endereco) {
            endereco.setId(enderecoNextId++);
            enderecoRepository.put(endereco.getId(),endereco);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(endereco);

        }

        @GetMapping
        public ResponseEntity<?> listEndereco(){
            return ResponseEntity.
                    status(HttpStatus.OK)
                    .body(this.enderecoRepository.values());
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> getEndereco(@PathVariable Long id){
            Endereco endereco = this.enderecoRepository.get(id);

            return (endereco != null ) ? ResponseEntity.ok(endereco) : ResponseEntity.notFound().build();
        }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEndereco(@PathVariable Long id, @RequestBody Endereco enderecoAtualizado) {
        if (!enderecoRepository.containsKey(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Endereço não encontrado");
        }

        Endereco enderecoExistente = enderecoRepository.get(id);

        enderecoExistente.setBairro(enderecoAtualizado.getBairro());
        enderecoExistente.setCep(enderecoAtualizado.getCep());
        enderecoExistente.setCidade(enderecoAtualizado.getCidade());
        enderecoExistente.setComplemento(enderecoAtualizado.getComplemento());
        enderecoExistente.setEstado(enderecoAtualizado.getEstado());
        enderecoExistente.setLogradouro(enderecoAtualizado.getLogradouro());
        enderecoExistente.setNumero(enderecoAtualizado.getNumero());
        enderecoExistente.setPais(enderecoAtualizado.getPais());

        enderecoRepository.put(id, enderecoExistente);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(enderecoExistente);
    }


    @PatchMapping
        public ResponseEntity<?> updateNumeroCompleto (@PathVariable Long id, @RequestBody Map<String, Object> updates
        ) {
            if(!this.enderecoRepository.containsKey(id)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Endereço não encontrado");
            }

            Endereco enderecoExistente = this.enderecoRepository.get(id);

            if(updates.containsKey("numero")) {
                enderecoExistente.setNumero((String) updates.get("numero"));

            }

            if(updates.containsKey("complemento")) {
                enderecoExistente.setComplemento((String) updates.get("complemento"));
            }

            this.enderecoRepository.put(id, enderecoExistente);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(enderecoExistente);
        }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndereco (@PathVariable Long id) {
        Endereco enderecoExistente = this.enderecoRepository.get(id);

        return (enderecoExistente != null) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }


}
