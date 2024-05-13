package project.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.blog.domain.User;
import project.blog.repository.UserRepository;

@RequiredArgsConstructor // 왜 이 생성자를 사용하는지!
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) { // 사용자 정보
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException((email)));
    }
}
