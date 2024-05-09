package project.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.blog.domain.Article;
import project.blog.dto.AddArticleRequest;
import project.blog.dto.UpdateArticleRequest;
import project.blog.repository.BlogRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화 & 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }


    @Test
    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    public void addArticle() throws Exception {
        // Given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        // JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // when
        // 설정 내용 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url) // 글 추가 API에 요청
                .contentType(MediaType.APPLICATION_JSON_VALUE) // 요청 타입 JSON
                .content(requestBody)); // 객체 요청

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        Assertions.assertThat(articles.size()).isEqualTo(1);
        Assertions.assertThat(articles.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(articles.get(0).getTitle()).contains("tit");
        Assertions.assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    public void findAllArticles() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }
    
    @Test
    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    public void findArticle() throws Exception {
        // given : 블로그 글 저장
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        
        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());
        // when : 지정한 블로그 글의 id 값으로 API 호출
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));
        // then : 응답 코드 200, content 와 title이 일치한지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    @Test
    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    public void deleteArticle() throws Exception {
        // given : 블로그 글 삭제
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when : 저장한 id 값으로 삭제 API 호출
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());
        // then : 200 Ok, 전체 글을 조회하고 배열 크기가 0인지 확인
        List<Article> articles = blogRepository.findAll();
        Assertions.assertThat(articles.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    public void updateArticle() throws Exception {
        // given : 블로그 글을 저장하고, 글 수정에 필요한 요청 객체 생성
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final String newTitle = "newTitle";
        final String newContent = "newContent";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when : UPDATE로 수정 요청 보냄, 타입 JSON, given 절에서 미리 만들어둔 객체를 요청 본문으로 함께 보냄
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // then : 응답 200 Ok, 블로그 글 id로 조회후 수정되었는지 확인
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();
        Assertions.assertThat(article.getTitle()).isEqualTo(newTitle);
        Assertions.assertThat(article.getContent()).isEqualTo(newContent);

    }
}