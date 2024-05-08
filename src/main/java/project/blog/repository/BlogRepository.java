package project.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.domain.Article;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
