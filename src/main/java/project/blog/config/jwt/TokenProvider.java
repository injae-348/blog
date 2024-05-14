package project.blog.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import project.blog.domain.User;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JWTProperties jwtProperties;

    // 토큰 생성
    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()),user);
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Date expiry, User user){
        Date now = new Date();
        
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ:JWT
                .setIssuer(jwtProperties.getIssuer()) // JWTProperties에 설정한 iss
                .setIssuedAt(now) // iat: 현재 시각
                .setExpiration(expiry) // exp: expiry 멤버 변수 값
                .setSubject(user.getEmail()) // sub : 유저 이메일
                .claim("id",user.getId()) // 클레임 id : 유져 Id
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 비밀값과 함께 HS256 방식 암호화
                .compact();
    }
    
    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀 값으로 복호화
                    .parseClaimsJws(token); // 검증
            return true;
        } catch (Exception e) { // 복호화 과정에서 에러가 있으면 유효하지 않은 토큰
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject
                (), "", authorities), token, authorities);
    }



    // 토큰 기반으로 유저 ID 가져오기
    public Long getUserId(String token){
        Claims claims = getClaims(token); // JWT의 내용
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey()) // 서명키 설정
                .parseClaimsJws(token) // 주어진 토큰 파싱, 검증
                .getBody(); // 클레임 반환
    }

}
