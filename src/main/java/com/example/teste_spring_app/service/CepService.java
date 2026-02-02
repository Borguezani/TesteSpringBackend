package com.example.teste_spring_app.service;

import com.example.teste_spring_app.dto.EnderecoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CepService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";

    public EnderecoDTO buscarEnderecoPorCep(String cep) {
        String cepLimpo = cep.replaceAll("[^0-9]", "");

        if (cepLimpo.length() != 8) {
            throw new RuntimeException("CEP inválido");
        }

        EnderecoDTO endereco = restTemplate.getForObject(VIA_CEP_URL, EnderecoDTO.class, cepLimpo);

        if (endereco == null || endereco.isErro()) {
            throw new RuntimeException("CEP não encontrado");
        }

        return endereco;
    }
}