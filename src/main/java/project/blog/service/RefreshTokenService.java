package project.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.blog.domain.RefreshToken;
import project.blog.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
