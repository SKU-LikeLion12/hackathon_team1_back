package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.ArticleRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberService memberService;

    @Transactional
    public Article saveNewArticle(String writerId, String title, String content, byte[] image) {
        Member member = memberService.findByUserId(writerId);
        Article article = new Article(title, content, member, image);
        articleRepository.saveNewArticle(article);
        return article;
    }

    @Transactional
    public Article updateArticle(Long articleId, String title, String content, String token, MultipartFile image) throws IOException {
        Member member = memberService.tokentoMember(token);
        Article article = articleRepository.findById(articleId);

        byte[] images = null;

        if(image != null) {
            // 사용자가 이미지를 올렸을때
            images = image.getBytes();
        } else {
            // 사용자가 이미지를 올리지 않았을때 기존에 저장되어 있는 이미지 사용
            images = article.getArticleImage();
        }

        if (member == article.getWriter()) {
            article.update(title, content, images);
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
