package com.example.teste_spring_app.service;

import com.example.teste_spring_app.dto.AuthResponseDTO;
import com.example.teste_spring_app.dto.LoginDTO;
import com.example.teste_spring_app.dto.RegistroDTO;
import com.example.teste_spring_app.entity.RefreshToken;
import com.example.teste_spring_app.entity.Usuario;
import com.example.teste_spring_app.repository.RefreshTokenRepository;
import com.example.teste_spring_app.repository.UsuarioRepository;
import com.example.teste_spring_app.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Transactional
    public AuthResponseDTO registrar(RegistroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuarioRepository.save(usuario);

        return gerarTokens(usuario);
    }

    @Transactional
    public AuthResponseDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return gerarTokens(usuario);
    }

    @Transactional
    public AuthResponseDTO refreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (token.isRevogado() || token.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expirado");
        }

        return gerarTokens(token.getUsuario());
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(token -> {
            token.setRevogado(true);
            refreshTokenRepository.save(token);
        });
    }

    private AuthResponseDTO gerarTokens(Usuario usuario) {
        String accessToken = jwtUtil.generateToken(usuario.getEmail());
        String refreshToken = UUID.randomUUID().toString();

        RefreshToken refresh = new RefreshToken();
        refresh.setToken(refreshToken);
        refresh.setUsuario(usuario);
        refresh.setDataExpiracao(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));
        refreshTokenRepository.save(refresh);

        return new AuthResponseDTO(accessToken, refreshToken, "Bearer", usuario.getEmail(), usuario.getNome());
    }
}