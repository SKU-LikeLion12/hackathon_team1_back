package spring.likelionpractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.likelionpractice.DTO.ArticleDTO.*;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.service.ArticleService;
import spring.likelionpractice.service.JwtUtility;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final JwtUtility jwtUtility;

    @GetMapping("/article/{id}")
    public ResponseArticle getArticle(@PathVariable("id") Long id) {
        Article article = articleService.findArticle(id);
        return new ResponseArticle(article);        // 흠.......
    }

    @PostMapping("/article/add")
    public ResponseArticle addArticle(@RequestBody requestArticle request) {
        String userId = jwtUtility.validateToken(request.getToken()).getSubject();
        Article article = articleService.saveNewArticle(userId, request.getTitle(), request.getContent());
        return new ResponseArticle(article);
    }

    @PutMapping("/article/{id}")
    public ResponseArticle updateArticle(@PathVariable("id") Long id, @RequestBody requestArticle request) {
        Article article = articleService.updateArticle(id, request.getTitle(), request.getContent(), request.getToken());
        return new ResponseArticle(article);
    }

    @DeleteMapping("/article/{id}")
    public void deleteArticle(@PathVariable("id") Long id, @RequestBody requestArticle request) {
        articleService.deleteArticle(id, request.getToken());
    }

    @GetMapping("/articles/all")
    public List<ResponseArticle> getAllArticles() {
        List<ResponseArticle> responseArticles = new ArrayList<>();
        for(Article article : articleService.findAllArticle()) {
            responseArticles.add(new ResponseArticle(article));
        }
        return responseArticles;
    }

    @GetMapping("/articles/all/{userid}")
    public List<ResponseArticle> getAllArticlesByUserId(@PathVariable("memberId") String memberId) {
        List<ResponseArticle> responseArticles = new ArrayList<>();
        for(Article article : articleService.findUserArticle(memberId)) {
            responseArticles.add(new ResponseArticle(article));
        }
        return responseArticles;
    }
}
