package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.ArticleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberService memberService;


    @Transactional
    public Article saveNewArticle(String writerId, String title, String content) {
        Member member = memberService.findByUserId(writerId);
        Article article = new Article(title, content, member);
        articleRepository.saveNewArticle(article);
        return article;
    }

    @Transactional
    public Article updateArticle(Long articleId, String title, String content, String token) {
        Member member = memberService.tokentoMember(token);
        Article article = articleRepository.findById(articleId);
        if (member == article.getWriter()) {
            article.update(title, content);
        }
        return article;
    }

    @Transactional
    public void deleteArticle(Long articleId, String token) {
        Member member = memberService.tokentoMember(token);
        Article article = articleRepository.findById(articleId);
        if (member == article.getWriter()) {
            articleRepository.deleteArticle(article);
        }
    }

    @Transactional
    public Article findArticle(Long articleId) {
//        Article article = articleRepository.findById(articleId);
//        return article;
        return articleRepository.findById(articleId);
    }

    @Transactional
    public List<Article> findAllArticle() {
        return articleRepository.findAll();
    }

    @Transactional
    public List<Article> findUserArticle(String memberId) { // Member member
        Member member = memberService.findByUserId(memberId);
        return articleRepository.findUserAll(member.getId());
    }
}
