package spring.likelionpractice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.DTO.ArticleDTO.*;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.service.ArticleService;
import spring.likelionpractice.service.JwtUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final JwtUtility jwtUtility;

    @Operation(summary = "게시물 조회", description = "id로 조회", tags = "Article",
                responses = {@ApiResponse(responseCode = "200", description = "게시물 조회"),
                            @ApiResponse(responseCode = "404", description = "게시물을 찾지 못했습니다.")})
    @GetMapping("/article/{id}")
    public ResponseEntity<ResponseArticle> getArticle(@PathVariable("id") Long id) {
        Article article = articleService.findArticle(id);
        return ResponseEntity.ok(new ResponseArticle(article));
    }

    @Operation(summary = "게시물 생성", description = "header에 Bearer 토큰 필요, (title, content, token, image)", tags = "Article",
                responses = {@ApiResponse(responseCode = "201", description = "게시물 생성"),
                            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")})
    @PostMapping(value ="/article/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseArticle> addArticle(@RequestHeader("Authorization") String BearerToken, RequestImgArticle request) throws IOException {
        String token = BearerToken.replace("Bearer", "");
        String userId = jwtUtility.validateToken(token).getSubject();

        byte[] imageByte = null;

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            imageByte = request.getImage().getBytes();
        }

        Article article = articleService.saveNewArticle(userId, request.getTitle(), request.getContent(), imageByte);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseArticle(article));
    }

    @Operation(summary = "게시물 수정", description = "header에 Bearer 토큰 필요, 본인이 생성한 게시물 수정(title, content, image)", tags = "Article",
                responses = {@ApiResponse(responseCode = "200", description = "게시물 수정"),
                            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")})
    @PutMapping(value = "/article/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseArticle> updateArticle(@RequestHeader("Authorization") String BearerToken, @PathVariable("id") Long id, @RequestPart requestArticle request, @RequestPart(required = false) MultipartFile image) throws IOException {
        String token = BearerToken.replace("Bearer", "");
        Article article = articleService.updateArticle(id, request.getTitle(), request.getContent(), token, image);
        return ResponseEntity.ok(new ResponseArticle(article));
    }

    @Operation(summary = "게시물 삭제", description = "header에 Bearer 토큰 필요", tags = "Article",
                responses = {@ApiResponse(responseCode = "204", description = "게시물 삭제 true"),
                            @ApiResponse(responseCode = "409", description = "게시물을 찾지 못했습니다.")})
    @DeleteMapping("/article/{id}")
    public ResponseEntity<Boolean> deleteArticle(@RequestHeader("Authorization") String BearerToken, @PathVariable("id") Long id) {
        String token = BearerToken.replace("Bearer", "");
        articleService.deleteArticle(id, token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
    }

    @Operation(summary = "전체 게시물 조회", description = "게시물 전체 조회 리스트", tags = "Article",
                responses = {@ApiResponse(responseCode = "200", description = "게시물 전체 리스트 조회")})
    @GetMapping("/articles/all")
    public List<ResponseArticleList> getAllArticles() {
        List<ResponseArticleList> responseArticles = new ArrayList<>();
        for(Article article : articleService.findAllArticle()) {
            responseArticles.add(new ResponseArticleList(article));
        }
        return responseArticles;
    }

    @Operation(summary = "내가 생성한 게시물 전체 조회", description = "{userId}로 내가 생성한 게시물 조회", tags = "Article",
                responses = {@ApiResponse(responseCode = "200", description = "내가 생성한 게시물 조회")})
    @GetMapping("/articles/all/{userId}")
    public List<ResponseArticle> getAllArticlesByUserId(@PathVariable("userId") String userId) {
        List<ResponseArticle> responseArticles = new ArrayList<>();
        for(Article article : articleService.findUserArticle(userId)) {
            responseArticles.add(new ResponseArticle(article));
        }
        return responseArticles;
    }
}
