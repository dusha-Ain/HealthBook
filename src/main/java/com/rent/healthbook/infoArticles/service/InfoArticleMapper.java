package com.rent.healthbook.infoArticles.service;

import com.rent.healthbook.infoArticles.dto.InfoArticle;
import com.rent.healthbook.infoArticles.entity.InfoArticleEntity;
import com.rent.healthbook.web.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InfoArticleMapper {

    public InfoArticleEntity toEntity(InfoArticle article){
        if (article == null){
            throw new ValidationException("Article should be not empty");
        }
        return new InfoArticleEntity(
                article.id(),
                article.title(),
                article.content(),
                article.created(),
                article.isPublished(),
                article.idProfile()
        );
    }

    public InfoArticleEntity toEntity(InfoArticleEntity articleEntity, InfoArticle article){
        if (article == null){
            return articleEntity;
        }
        Optional.ofNullable(article.title()).ifPresent(articleEntity::setTitle);
        Optional.ofNullable(article.content()).ifPresent(articleEntity::setContent);
        Optional.ofNullable(article.idProfile()).ifPresent(articleEntity::setIdProfile);
        return articleEntity;
    }

    public InfoArticle toDomainInfoArticle(InfoArticleEntity article){
        if (article == null){
            throw new ValidationException("Article should be not empty");
        }
        return new InfoArticle(
              article.getId(),
              article.getTitle(),
              article.getContent(),
              article.getCreated(),
              article.getPublished(),
              article.getIdProfile()
        );
    }
}
