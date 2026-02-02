package com.example.teste_spring_app.controller;

import com.example.teste_spring_app.dto.EnderecoDTO;
import com.example.teste_spring_app.service.CepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cep")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "CEP", description = "Buscar endereço por CEP")
public class CepController {
    private final CepService cepService;

    @GetMapping("/{cep}")
    @Operation(summary = "Buscar endereço por CEP")
    public ResponseEntity<EnderecoDTO> buscarPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(cepService.buscarEnderecoPorCep(cep));
    }
}