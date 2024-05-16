package project.blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails { // UserDetails : 스프링 시큐리티에서 사용자의 인증
    // 정보를 담아두는 인터페이스, 해당 객체를 통해 인증 정보를 가져온다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    // 사용자 이름, oauth 관련키 저장
    @Column(name = "nickname", unique = true)
    private String nickname;

    @Builder
    public User(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override // 사용자 패스워드 반환
    public String getPassword() {
        return password;
    }

    @Override // 사용자 id 반환(고유값)
    public String getUsername() {
        return email;
    }

    @Override // 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인 로직
        return true; // true -> 만료되지 않았음
    }

    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        // 계정이 잠금되어 있는지 확인
        return true; // true -> 계정이 잠금되지 않았음
    }

    @Override // 패스워드 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인
        return true; // true -> 만료되지 않았음
    }

    @Override // 계정 사용 가능 여부 반환
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }

    // 사용자 이름 변경
    public User update(String nickname){
        this.nickname = nickname;

        return this;
    }
}
