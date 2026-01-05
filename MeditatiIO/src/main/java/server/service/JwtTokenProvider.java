package server.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
        private final String jwtSecret = "schimba_asta_cu_o_cheie_lunga_si_sigura_256bits";
        private final long jwtExpirationMs = 86400000; // 24h

        private Key getSigningKey() {
            return Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }

        //  Generează token după autentificare
        public String generateToken(Authentication authentication) {
            String username = authentication.getName();

            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        //  Extrage username-ul din token
        public String getUsernameFromToken(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }

        // Verifică dacă tokenul e valid
        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                System.out.println("JWT invalid sau expirat: " + e.getMessage());
                return false;
            }
        }
}
