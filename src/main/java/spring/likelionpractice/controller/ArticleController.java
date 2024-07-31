package spring.likelionpractice.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "게시물 조회", description = "{id}로 조회", tags = "Article")
    @GetMapping("/article/{id}")
    public ResponseArticle getArticle(@PathVariable("id") Long id) {
        Article article = articleService.findArticle(id);
        return new ResponseArticle(article);
    }

    @Operation(summary = "게시물 생성", description = "(title, content, token, image)", tags = "Article")
    @PostMapping(value ="/article/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseArticle addArticle(@RequestPart requestArticle request, @RequestPart MultipartFile image) throws IOException {
        String userId = jwtUtility.validateToken(request.getToken()).getSubject();

        byte[] imageByte = image.getBytes();

        Article article = articleService.saveNewArticle(userId, request.getTitle(), request.getContent(), imageByte);
        return new ResponseArticle(article);
    }

    @Operation(summary = "게시물 수정", description = "본인이 생성한 게시물 수정(title, content, token, image)", tags = "Article")
    @PutMapping(value = "/article/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseArticle> updateArticle(@PathVariable("id") Long id, @RequestPart requestArticle request, @RequestPart MultipartFile image) throws IOException {
        Article article = articleService.updateArticle(id, request.getTitle(), request.getContent(), request.getToken(), image);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseArticle(article));
    }

    @Operation(summary = "게시물 삭제", description = "본인이 생성한 게시물 삭제(token)", tags = "Article")
    @DeleteMapping("/article/{id}")
    public void deleteArticle(@PathVariable("id") Long id, @RequestBody requestArticle request) {
        articleService.deleteArticle(id, request.getToken());
    }

    @Operation(summary = "전체 게시물 조회", description = "게시물 전체 조회 리스트", tags = "Article")
    @GetMapping("/articles/all")
    public List<ResponseArticle> getAllArticles() {
        List<ResponseArticle> responseArticles = new ArrayList<>();
        for(Article article : articleService.findAllArticle()) {
            responseArticles.add(new ResponseArticle(article));
        }
        return responseArticles;
    }

    @Operation(summary = "내가 생성한 게시물 전체 조회", description = "{userId}로 내가 생성한 게시물 조회", tags = "Article")
    @GetMapping("/articles/all/{userId}")
    public List<ResponseArticle> getAllArticlesByUserId(@PathVariable("userId") String userId) {
        List<ResponseArticle> responseArticles = new ArrayList<>();
        for(Article article : articleService.findUserArticle(userId)) {
            responseArticles.add(new ResponseArticle(article));
        }
        return responseArticles;
    }
}
