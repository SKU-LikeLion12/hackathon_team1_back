package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.likelionpractice.Exception.InvalidCommentException;
import spring.likelionpractice.Exception.InvalidCredentialsException;
import spring.likelionpractice.Exception.InvalidIdException;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.domain.Comment;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final ArticleService articleService;

    @Transactional
    public Comment saveComment(String token, Long article_id, String content) {
        Member member = memberService.tokentoMember(token);

        if (member == null) {
            throw new InvalidCredentialsException();
        }

        Article article = articleService.findArticle(article_id);

        if (article == null) {
            throw new InvalidIdException();
        }

        Comment comment = new Comment(member, article, content);
        commentRepository.saveComment(comment);
        return comment;
    }

    @Transactional
    public List<Comment> articleToComment(Long articleId) {
        Article article = articleService.findArticle(articleId);
        return new ArrayList<>(commentRepository.findArticleComment(article));
    }

    @Transactional
    public boolean deleteComment(Long commentId, String token) {
        Comment comment = commentRepository.findById(commentId);
        Member member = memberService.tokentoMember(token);

        if(member == null) {
            throw new InvalidCredentialsException();
        }

        if(comment == null) {
            throw new InvalidIdException();
        }

        if(member == comment.getWriter()) { commentRepository.deleteComment(comment); return true; }
        else return false;
    }

    @Transactional
    public Comment updateComment(Long commentId, String token, String content) {
        Member member = memberService.tokentoMember(token);

        if (member == null) {
            throw new InvalidCredentialsException();
        }

        Comment comment = commentRepository.findById(commentId);

        if (comment == null) {
            throw new InvalidCommentException();
        }

        if (member == comment.getWriter()) {
            comment.updateComment(content);
        }
        return comment;
    }
}
