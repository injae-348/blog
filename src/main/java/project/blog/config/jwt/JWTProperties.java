package project.blog.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // java 클래스에 프로퍼티 값을 가져와서 사용하는 에너테이션
public class JWTProperties {
    private String issuer;
    private String secretKey;
}
