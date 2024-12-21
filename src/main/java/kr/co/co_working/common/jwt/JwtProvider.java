package kr.co.co_working.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    public Map<String, String> createTokens(String email) throws NoSuchElementException, Exception {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        Map<String, String> tokens = new HashMap<>();

        String accessToken = Jwts.builder()
            .header().add(Map.of("type", "JWT", "alg", "HS384")).and()
            .claims()
                .subject(CookieType.Authorization.name())
                .issuer(email)
                .expiration(new Date(System.currentTimeMillis() + (15 * 60 * 1000)))
                .issuedAt(new Date(System.currentTimeMillis())).and()
            .signWith(key)
            .compact();

        String refreshToken = Jwts.builder()
            .header().add(Map.of("type", "JWT", "alg", "HS384")).and()
            .claims()
                .subject(CookieType.RefreshToken.name())
                .issuer(email)
                .expiration(new Date(System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000)))
                .issuedAt(new Date(System.currentTimeMillis())).and()
            .signWith(key)
            .compact();

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    public String resolveToken(HttpServletRequest request, String cookieType) throws NoSuchElementException, Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieType.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public boolean validateToken(String token) throws JwtException, Exception {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

            return true;
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public Authentication getAuthentication(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        String email = claims.getIssuer();
        User member = new User(email, "", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(member, token, member.getAuthorities());
    }
}
