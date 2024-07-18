package spring.likelionpractice.service;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtility {
    private String secret = "yourSecretKey";

    private static final long EXPIRATION_TIME = 1000L * 60 * 60;        // 1시간으로 사용

    public String generateToken(String userId) {
        return Jwts.builder()               // 빌드 생성
                .setSubject(userId)         // 매개변수로 받은 토큰의 주체로 설정
                .setIssuedAt(new Date())    // 토큰의 발행 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))      // 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();

            return claims;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (Exception ex) {
            System.out.println("Invalid JWT token");
        }
        return null;
    }
}
