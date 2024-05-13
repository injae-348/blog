package project.blog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import project.blog.service.UserDetailService;

import java.net.http.HttpRequest;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    // secuirty와 같은 외부 라이브러리?는 수동 등록을 해줘야 한다!
    private final UserDetailService userDetailService;
    
    // spring security 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    @Bean // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeRequests()
                .requestMatchers("/login","/signup","/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/articles")
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .and()
                .csrf().disable()
                .build();
    }

    @Bean // 인증 관리자 관련 설정
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService)
        throws Exception{
            return http.getSharedObject(AuthenticationManagerBuilder.class)
                    .userDetailsService(userDetailService)
                    .passwordEncoder(bCryptPasswordEncoder)
                    .and()
                    .build();
    }

    @Bean // 패스워드 인코더로 사용할 빈 등록
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
