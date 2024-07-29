package spring.likelionpractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/comment")
    public CommentResponse createComment(@RequestBody CommentCreateRequest request) {
        Comment comment = commentService.saveComment(request.getToken(), request.getArticleId(), request.getContent(), request.getParentComment());
        return new CommentResponse(comment);
    }

    @PutMapping("/comment")
    public CommentResponse updateComment(@RequestBody CommentUpdateRequest request) {
        Comment comment = commentService.updateComment(request.getCommentId(), request.getToken(), request.getContent());
        return new CommentResponse(comment);
    }

    @DeleteMapping("/comment")
    public void deleteComment(@RequestBody CommentDeleteRequest request) {
        commentService.deleteComment(request.getCommentId(), request.getToken());
    }

    @GetMapping("/comment/article/{id}")
    public List<CommentResponse> articleComment(@PathVariable("id") Long articleId) {
        List<CommentResponse> responseComment = new ArrayList<>();
        for(Comment comment : commentService.articleToComment(articleId)) {
            responseComment.add(new CommentResponse(comment));
        }
        return responseComment;
    }

    @PostMapping("/comment/{parentId}/add-child") // 대댓글 조회
    public ResponseEntity<Comment> addChildComment(@PathVariable Long parentId, @RequestBody Comment childComment) {
        Comment parentComment = new Comment();
        parentComment.setId(parentId);
        Comment savedChildComment = commentService.addChildComment(parentComment, childComment);
        return ResponseEntity.ok(savedChildComment);
    }

    @GetMapping("/comment/child-comments/{parentId}") // 대댓글 생성
    public List<Comment> getChildComments(@PathVariable Long parentId) {
        Comment parentComment = new Comment();
        parentComment.setId(parentId);
        return commentService.getChildComments(parentComment);
    }
}
