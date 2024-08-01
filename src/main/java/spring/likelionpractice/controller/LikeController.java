package spring.likelionpractice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "로그인한 후 게시물에 대한 좋아요", description = "header에 Bearer 토큰 필요, 게시물에 대한 id 필요", tags = "like",
                responses = {@ApiResponse(responseCode = "200", description = "게시물 좋아요 + 1, 다시 누르면 -1"),
                            @ApiResponse(responseCode = "400", description = "Id를 찾을 수 없습니다.")})
    @PostMapping("/like/{id}")       // 유저당 게시글에 좋아요 1개(한번 더 누르면 좋아요 취소)
    public ResponseEntity<LikeResponse> like(@RequestHeader("Authorization") String BearerToken, @PathVariable Long id) {
        String token = BearerToken.replace("Bearer", "");
        Member member = memberService.tokentoMember(token);
        boolean liked = likeService.clickLike(id, member.getId());
        Long likeCount = likeService.findByArticleIdLikeCount(id);

        return ResponseEntity.ok(new LikeResponse(liked, likeCount));
    }

    @Operation(summary = "해당 게시글의 좋아요 개수", description = "게시글에 대한 id 필요", tags = "like",
                responses = {@ApiResponse(responseCode = "200", description = "게시물 좋아요 개수"),
                            @ApiResponse(responseCode = "400", description = "Id를 찾지 못했습니다.")})
    @GetMapping("/like/{id}")       // 게시글 하나 불러올 때 해당 게시글의 좋아요 개수 불러오기
    public ResponseEntity<CountResponse> getLike(@PathVariable Long id) {
        Long likeCount = likeService.findByArticleIdLikeCount(id);
        return ResponseEntity.ok(new CountResponse(likeCount));
    }

    @Operation(summary = "로그인한 사용자가 좋아요한 게시물 목록 조회", description = "header에 Bearer토큰 필요", tags = "like",
                responses = {@ApiResponse(responseCode = "200", description = "내가 좋아요한 게시물 제목, 게시물 좋아요 수 불러오기"),
                            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")})
    @GetMapping("/like/me")     // 내가 좋아요한 게시글 목록 조회
    public ResponseEntity<List<LikeCountResponse>> getMyLikeArticle(@RequestHeader("Authorization") String BearerToken) {
        String token = BearerToken.replace("Bearer", "");
        Member member = memberService.tokentoMember(token);
        List<LikeCountResponse> likeMyArticleRequests = new ArrayList<>();
        for(Article article : likeService.findByArticleLikeMe(member)) {
            likeMyArticleRequests.add(new LikeCountResponse(article));
        }
        return ResponseEntity.ok(likeMyArticleRequests);
    }

    @Operation(summary = "게시글 별 좋아요 개수 불러오기", tags = "like",
                responses = {@ApiResponse(responseCode = "200", description = "모든 게시물 좋아요 개수, 제목 불러오기")})
    @GetMapping("/likes")           // 게시물 리스트 불러올 때 각 게시글 별 좋아요 개수 불러오기
    public ResponseEntity<List<LikeCountResponse>> getLikeCount() {
        List<LikeCountResponse> responseLikeCount = new ArrayList<>();
        for(Article article : likeService.findArticleLikeAll()) {
            responseLikeCount.add(new LikeCountResponse(article));
        }
        return ResponseEntity.ok(responseLikeCount);
    }

    // 게시글 좋아요 개수 내림차순 정렬 조회(좋아요 많은거부터)
    @Operation(summary = "게시글 별 좋아요 내림차순 정렬", tags = "like",
                responses = {@ApiResponse(responseCode = "200", description = "모든 게시물 좋아요 개수 내림차순, 제목 불러오기")})
    @GetMapping("/likeSort")
    public ResponseEntity<List<LikeCountResponse>> getLikeCountSort() {
        List<LikeCountResponse> responseLikeSortCount = new ArrayList<>();
        for(Article article : likeService.findBySortLike()) {
            responseLikeSortCount.add(new LikeCountResponse(article));
        }
        return ResponseEntity.ok(responseLikeSortCount);
    }
}