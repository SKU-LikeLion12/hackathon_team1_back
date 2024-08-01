package spring.likelionpractice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.likelionpractice.DTO.LikeDTO;
import spring.likelionpractice.DTO.LikeDTO.*;
import spring.likelionpractice.Exception.InvalidCredentialsException;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.service.LikeService;
import spring.likelionpractice.service.MemberService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final MemberService memberService;

    @Operation(summary = "로그인한 후 게시물에 대한 좋아요", description = "게시물에 대한 좋아요", tags = "like")
    @PostMapping("/like")       // 유저당 게시글에 좋아요 1개(한번 더 누르면 좋아요 취소)
    public ResponseEntity<LikeResponse> like(@RequestBody LikeRequest request) {
        try {
            Member member = memberService.tokentoMember(request.getToken());
            boolean liked = likeService.clickLike(request.getArticleId(), member.getId());
            Long likeCount = likeService.findByArticleIdLikeCount(request.getArticleId());

            return ResponseEntity.ok(new LikeResponse(liked, likeCount));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Operation(summary = "해당 게시글의 좋아요 개수", description = "게시글 좋아요 개수", tags = "like")
    @GetMapping("/like/{id}")       // 게시글 하나 불러올 때 해당 게시글의 좋아요 개수 불러오기
    public LikeResponse getLike(@PathVariable Long id, @RequestBody LikeRequest request) {
        Member member = memberService.tokentoMember(request.getToken());
        boolean liked = likeService.clickLike(id, member.getId());
        Long likeCount = likeService.findByArticleIdLikeCount(id);
        return new LikeResponse(liked, likeCount);
    }

    @Operation(summary = "로그인한 사용자가 좋아요한 게시물 목록 조회", tags = "like")
    @GetMapping("/like/me")     // 내가 좋아요한 게시글 목록 조회
    public List<LikeCountResponse> getMyLikeArticle(@RequestBody LikeRequest request) {
        Member member = memberService.tokentoMember(request.getToken());
        List<LikeCountResponse> likeMyArticleRequests = new ArrayList<>();
        for(Article article : likeService.findByArticleLikeMe(member)) {
            likeMyArticleRequests.add(new LikeCountResponse(article));
        }
        return likeMyArticleRequests;
    }

    @Operation(summary = "게시글 별 좋아요 개수 불러오기", tags = "like")
    @GetMapping("/likes")           // 게시물 리스트 불러올 때 각 게시글 별 좋아요 개수 불러오기
    public ResponseEntity<List<LikeCountResponse>> getLikeCount() {
        List<LikeCountResponse> responseLikeCount = new ArrayList<>();
        for(Article article : likeService.findArticleLikeAll()) {
            responseLikeCount.add(new LikeCountResponse(article));
        }
        return ResponseEntity.ok(responseLikeCount);
    }

    // 게시글 좋아요 개수 내림차순 정렬 조회(좋아요 많은거부터)
    @Operation(summary = "게시글 별 좋아요 내림차순 정렬", tags = "like")
    @GetMapping("/likeSort")
    public List<LikeCountResponse> getLikeCountSort() {
        List<LikeCountResponse> responseLikeSortCount = new ArrayList<>();
        for(Article article : likeService.findBySortLike()) {
            responseLikeSortCount.add(new LikeCountResponse(article));
        }
        return responseLikeSortCount;
    }
}