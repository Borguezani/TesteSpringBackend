package com.example.teste_spring_app.controller;

import com.example.teste_spring_app.dto.ClienteDTO;
import com.example.teste_spring_app.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Clientes", description = "CRUD de clientes (requer autenticação)")
public class ClienteController {
    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Criar novo cliente")
    public ResponseEntity<ClienteDTO> criar(@Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(clienteService.criar(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes")
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(clienteService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.ok().build();
    }
}