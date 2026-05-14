package com.rent.healthbook.infoArticles.service;

import com.rent.healthbook.infoArticles.dao.InfoArticlesDao;
import com.rent.healthbook.infoArticles.dto.InfoArticle;
import com.rent.healthbook.infoArticles.dto.InfoArticles;
import com.rent.healthbook.infoArticles.entity.InfoArticleEntity;
import com.rent.healthbook.web.helpers.AppConfig;
import com.rent.healthbook.web.helpers.Pageable;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
public class InfoArticlesService {

    private final InfoArticleMapper mapper;
    private final InfoArticlesDao articleDao;

    public InfoArticlesService(InfoArticleMapper mapper, InfoArticlesDao articleDao) {
        this.mapper = mapper;
        this.articleDao = articleDao;
    }

    @Transactional
    public InfoArticle addArticle(@Valid InfoArticle article) {
        var articleEntity = mapper.toEntity(article);
        var idCreated = InfoArticlesValidator.validateCreationResult(
                articleDao.createArticle(articleEntity));
        var articleCreated =
                InfoArticlesValidator.validateArticleCheckExists(
                        articleDao.getArticleById(idCreated), idCreated);
        return mapper.toDomainInfoArticle(articleCreated);
    }

    public InfoArticle getArticleById(UUID id) {
        InfoArticleEntity findArticle =
                InfoArticlesValidator.validateArticleCheckExists(articleDao.getArticleById(id), id);
        return mapper.toDomainInfoArticle(findArticle);
    }

    public InfoArticles getAllDoctorsByFilter(Pageable filter) {
        filter.setPageSize(Objects.requireNonNullElse(filter.getPageSize(), AppConfig.getDefaultPageSize()));
        filter.setPageNumber(Objects.requireNonNullElse(filter.getPageNumber(), AppConfig.getDefaultPageNumber()));
        var articlesEntity = articleDao.getAllByFilter(filter);
        return new InfoArticles(articlesEntity.list.stream()
                .map(mapper::toDomainInfoArticle).toList());
    }

    @Transactional
    public InfoArticle updateArticle(UUID id, @Valid InfoArticle article) {
        InfoArticleEntity findArticle =
                InfoArticlesValidator.validateArticleCheckExists(articleDao.getArticleByIdWithLock(id), id);
        var updateArticle = mapper.toEntity(findArticle, article);
        InfoArticlesValidator.validateUpdateResult(articleDao.updateArticle(updateArticle), id);
        InfoArticleEntity savedArticle =
                InfoArticlesValidator.validateArticleExistsAfterUpdate(articleDao.getArticleById(id), id);
        return mapper.toDomainInfoArticle(savedArticle);
    }

    @Transactional
    public void updateStatusArticle(UUID id, @Valid Boolean status) {
        InfoArticlesValidator.validateArticleCheckExists(articleDao.getArticleByIdWithLock(id), id);
        InfoArticlesValidator.validateUpdateStatusResult(articleDao.updateStatusArticle(id, status), id);
    }

    @Transactional
    public void deleteArticle(UUID id) {
        InfoArticlesValidator.validateArticleCheckExists(articleDao.getArticleByIdWithLock(id), id);
        InfoArticlesValidator.validateDeleteResult(articleDao.deleteArticle(id), id);
    }
}
