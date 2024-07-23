package spring.likelionpractice.repository;

import spring.likelionpractice.domain.ArticleImage;

import java.util.List;

public interface ArticleImageRepository {

    public ArticleImage save(ArticleImage articleImage);    // 게시물 이미지 경로 저장

    public void deleteArticleImage(ArticleImage articleImage);

    public ArticleImage getArticleImage(Long articleImageId);

    public List<ArticleImage> getAllArticleImages();
}
