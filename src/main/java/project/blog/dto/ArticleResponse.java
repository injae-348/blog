package project.blog.dto;

import lombok.Getter;
import project.blog.domain.Article;

// 요청 받을시 사용할 DTO => 아래와 같은 형식으로 데이터가 전달
@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
    
    public ArticleResponse(Article article){
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
