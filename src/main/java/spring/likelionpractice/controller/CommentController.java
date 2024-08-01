package spring.likelionpractice.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "게시물에 대한 댓글 생성", description = "게시물에 대한 댓글 생성", tags = "Comment")
    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentCreateRequest request) {
        try {
            Comment comment = commentService.saveComment(request.getToken(), request.getArticleId(), request.getContent());
            return ResponseEntity.ok(new CommentResponse(comment));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Operation(summary = "게시물에 대한 내가 쓴 댓글 수정", description = "댓글 수정(content, token, commentId ", tags = "Comment")
    @PutMapping("/comment")
    public ResponseEntity<CommentResponse> updateComment(@RequestBody CommentUpdateRequest request) {
        try {
            Comment comment = commentService.updateComment(request.getCommentId(), request.getToken(), request.getContent());
            return ResponseEntity.ok(new CommentResponse(comment));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Operation(summary = "게시물에 대한 내가 쓴 댓글 삭제", description = "댓글 삭제(token commentId)", tags = "Comment")
    @DeleteMapping("/comment")
    public ResponseEntity<String> deleteComment(@RequestBody CommentDeleteRequest request) {
        try {
            commentService.deleteComment(request.getCommentId(), request.getToken());
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        }
    }

    @Operation(summary = "게시물에 대한 댓글 조회", description = "게시물에 대한 댓글 조회(articleId)", tags = "Comment")
    @GetMapping("/comment/article/{id}")
    public List<CommentResponse> articleComment(@PathVariable("id") Long articleId) {
        List<CommentResponse> responseComment = new ArrayList<>();
        for(Comment comment : commentService.articleToComment(articleId)) {
            responseComment.add(new CommentResponse(comment));
        }
        return responseComment;
    }
}
