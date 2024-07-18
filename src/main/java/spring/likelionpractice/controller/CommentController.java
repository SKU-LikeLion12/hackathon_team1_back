package spring.likelionpractice.controller;

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

    @PostMapping("/comment")
    public CommentResponse createComment(@RequestBody CommentCreateRequest request) {
        Comment comment = commentService.saveComment(request.getToken(), request.getArticleId(), request.getContent());
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
}
