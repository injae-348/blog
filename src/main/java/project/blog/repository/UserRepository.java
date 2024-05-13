package project.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 이메일로 사용자 정보 가져오기
}
