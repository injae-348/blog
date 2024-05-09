package project.blog.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.blog.domain.Article;
import project.blog.dto.AddArticleRequest;
import project.blog.dto.UpdateArticleRequest;
import project.blog.repository.BlogRepository;

import java.util.List;

@RequiredArgsConstructor // final 또는 @NotNull 이 붙은 필드의 생성자 추가
@Service // 빈으로 등록 - @Component포함
public class BlogService {
    // @Autowired 생략
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
    
    // 글 조회 메서드
    public List<Article> findAll() {
        return blogRepository.findAll();
    }
    
    // 상세 조회
    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    // 삭제
    public void delete(Long id){
        blogRepository.deleteById(id);
    }

    // 수정
    @Transactional
    public Article update(Long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id).orElseThrow(()->new IllegalArgumentException("not found : " + id));

        article.update(request.getTitle(),request.getContent());
        return article;
    }
}
