package spring.likelionpractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.DTO.ArticleDTO.*;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.service.ArticleService;
import spring.likelionpractice.service.ImageUtility;
import spring.likelionpractice.service.JwtUtility;

import java.io.IOException;
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

    @PostMapping(value ="/article/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseArticle addArticle(@RequestPart requestArticle request, @RequestPart MultipartFile image) throws IOException {
        String userId = jwtUtility.validateToken(request.getToken()).getSubject();

        byte[] imagebyte = image.getBytes();

        Article article = articleService.saveNewArticle(userId, request.getTitle(), request.getContent(), imagebyte);
        return new ResponseArticle(article);
    }

    @PutMapping(value = "/article/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseArticle> updateArticle(@PathVariable("id") Long id, @RequestPart requestArticle request, @RequestPart MultipartFile image) throws IOException {
        Article article = articleService.updateArticle(id, request.getTitle(), request.getContent(), request.getToken(), image);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseArticle(article));
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
