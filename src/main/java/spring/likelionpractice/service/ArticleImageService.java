package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.domain.ArticleImage;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.ArticleImageRepository;
import spring.likelionpractice.repository.ArticleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleImageService {

    private final ArticleImageRepository articleImageRepository;
    private final ArticleRepository articleRepository;
    private final MemberService memberService;

    @Transactional
    public ArticleImage saveArticleImage(Long articleId, String imageUrl) {
        Article article = articleRepository.findById(articleId);
        ArticleImage articleImg = new ArticleImage(imageUrl, article);
        return articleImageRepository.save(articleImg);
    }

    @Transactional
    public ArticleImage updateArticleImage(Long articleId, String imageUrl, String token) {
        Member member = memberService.tokentoMember(token);
        ArticleImage articleimg = articleImageRepository.getArticleImage(articleId);
        if (member == articleimg.getArticle().getWriter()) {
            articleimg.update(imageUrl);
        }
        return articleimg;
    }

    @Transactional
    public void deleteArticleImage(Long articleId, String imageUrl, String token) {
        Member member = memberService.tokentoMember(token);
        ArticleImage articleimg = articleImageRepository.getArticleImage(articleId);
        if (member == articleimg.getArticle().getWriter()) {
            articleImageRepository.deleteArticleImage(articleimg);
        }
    }

    @Transactional
    public List<ArticleImage> findAllArticleImages(Long articleId) {
        return articleImageRepository.getAllArticleImages();
    }
}
