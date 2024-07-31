package spring.likelionpractice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.likelionpractice.DTO.CommentDTO.*;
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
    public CommentResponse createComment(@RequestBody CommentCreateRequest request) {
        Comment comment = commentService.saveComment(request.getToken(), request.getArticleId(), request.getContent());
        return new CommentResponse(comment);
    }

    @Operation(summary = "게시물에 대한 내가 쓴 댓글 수정", description = "댓글 수정(content, token, commentId ", tags = "Comment")
    @PutMapping("/comment")
    public CommentResponse updateComment(@RequestBody CommentUpdateRequest request) {
        Comment comment = commentService.updateComment(request.getCommentId(), request.getToken(), request.getContent());
        return new CommentResponse(comment);
    }

    @Operation(summary = "게시물에 대한 내가 쓴 댓글 삭제", description = "댓글 삭제(token commentId)", tags = "Comment")
    @DeleteMapping("/comment")
    public void deleteComment(@RequestBody CommentDeleteRequest request) {
        commentService.deleteComment(request.getCommentId(), request.getToken());
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
