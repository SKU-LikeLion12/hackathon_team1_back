package spring.likelionpractice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.likelionpractice.DTO.CommentDTO.*;
import spring.likelionpractice.Exception.InvalidCredentialsException;
import spring.likelionpractice.domain.Comment;
import spring.likelionpractice.service.CommentService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "게시물에 대한 댓글 생성", description = "header에 Bearer 토큰 필요, 게시물에 대한 댓글 생성(articleId, content 필요)", tags = "Comment",
                responses = {@ApiResponse(responseCode = "201", description = "댓글 생성")})
    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> createComment(@RequestHeader("Authorization") String BearerToken, @RequestBody CommentCreateRequest request) {
        String token = BearerToken.replace("Bearer", "");
        Comment comment = commentService.saveComment(token, request.getArticleId(), request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponse(comment));
    }

    @Operation(summary = "게시물에 대한 내가 쓴 댓글 수정", description = "header에 bearer 토큰 필요, 댓글 수정(content, commentId 필요)", tags = "Comment",
                responses = {@ApiResponse(responseCode = "200", description = "댓글 수정"),
                            @ApiResponse(responseCode = "400", description = "댓글을 찾지 못하였습니다.")})
    @PutMapping("/comment")
    public ResponseEntity<CommentResponse> updateComment(@RequestHeader("Authorization") String BearerToken, @RequestBody CommentUpdateRequest request) {
        String token = BearerToken.replace("Bearer", "");
        Comment comment = commentService.updateComment(request.getCommentId(), token, request.getContent());
        return ResponseEntity.ok(new CommentResponse(comment));
    }

    @Operation(summary = "게시물에 대한 내가 쓴 댓글 삭제", description = "Authorization Bearer 토큰 필요 댓글 삭제(commentId 필요)", tags = "Comment",
                responses = {@ApiResponse(responseCode = "200", description = "댓글이 삭제되었습니다."),
                            @ApiResponse(responseCode = "400", description = "댓글을 찾지 못하였습니다.")})
    @DeleteMapping("/comment")
    public ResponseEntity<String> deleteComment(@RequestHeader("Authorization") String BearerToken, @RequestBody CommentDeleteRequest request) {
        String token = BearerToken.replace("Bearer", "");
        commentService.deleteComment(request.getCommentId(), token);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    @Operation(summary = "게시물에 대한 댓글 조회", description = "게시물에 대한 댓글 조회(articleId 필요)", tags = "Comment",
                responses = {@ApiResponse(responseCode = "200", description = "게시글에 대한 댓글 조회")})
    @GetMapping("/comment/article/{id}")
    public ResponseEntity<List<CommentResponse>> articleComment(@PathVariable("id") Long articleId) {
        List<CommentResponse> responseComment = new ArrayList<>();
        for(Comment comment : commentService.articleToComment(articleId)) {
            responseComment.add(new CommentResponse(comment));
        }
        return ResponseEntity.ok(responseComment);
    }


}
