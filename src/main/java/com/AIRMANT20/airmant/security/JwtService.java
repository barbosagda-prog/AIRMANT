package com.AIRMANT20.airmant.security;

import com.AIRMANT20.airmant.model.Usuarios;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtService(@Value("${airmant.jwt.secret}") String secreto,
                      @Value("${airmant.jwt.expiracion-horas}") long horas) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = horas * 60 * 60 * 1000;
    }

    public String generarToken(Usuarios usuario) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(String.valueOf(usuario.getId_usuarios()))
                .claim("rol", usuario.getRol())
                .claim("nombre", usuario.getNombres())
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expiracionMs))
                .signWith(clave)
                .compact();
    }

    public Optional<Claims> validar(String token) {
        try {
            return Optional.of(Jwts.parser().verifyWith(clave).build()
                    .parseSignedClaims(token).getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
