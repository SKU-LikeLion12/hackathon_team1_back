package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.domain.ArticleImage;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleImageService {

    private final ArticleService articleService;

    @Transactional
    public ArticleImage saveImage(MultipartFile file, Long articleId) {     // 이미지를 저장
        Article article_id = articleService.findArticle(articleId);
        byte[] fileName = file.getOriginalFilename().getBytes();
        ArticleImage articleImage = new ArticleImage(fileName, article_id);
        return articleImage;
    }
}
