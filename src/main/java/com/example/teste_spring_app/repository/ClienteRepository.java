package com.example.teste_spring_app.repository;

import com.example.teste_spring_app.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
}