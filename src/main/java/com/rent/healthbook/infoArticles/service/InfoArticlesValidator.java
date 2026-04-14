package com.rent.healthbook.infoArticles.service;

import com.rent.healthbook.infoArticles.entity.InfoArticleEntity;
import com.rent.healthbook.web.exception.EntityNotFoundInternalException;
import com.rent.healthbook.web.exception.EntityToManyResultException;
import com.rent.healthbook.web.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class InfoArticlesValidator {

    public static InfoArticleEntity validateArticleExistsAfterUpdate(
            Optional<InfoArticleEntity> article, UUID id) {
        return article.orElseThrow(() -> new EntityNotFoundInternalException(
                String.format("Article not found after update by id = %s", id)));
    }

    public static InfoArticleEntity validateArticleCheckExists(
            Optional<InfoArticleEntity> article, UUID id) {
        return article.orElseThrow(() -> new ValidationException(
                String.format("Article not found by id = %s", id)));
    }

    public static UUID validateCreationResult(UUID id) {
        if (id == null) {
            throw new ValidationException("Article creation failed");
        }
        return id;
    }

    public static void validateUpdateResult(boolean updateResult, UUID id) {
        if (!updateResult) {
            throw new ValidationException(
                    String.format("Article update failed by id = %s", id));
        }
    }

    public static void validateUpdateStatusResult(boolean updateResult, UUID id) {
        if (!updateResult) {
            throw new ValidationException(
                    String.format("Article update status failed by id = %s", id));
        }
    }

    public static void validateDeleteResult(boolean deleteResult, UUID id) {
        if (!deleteResult) {
            throw new ValidationException(
                    String.format("Article delete failed by id = %s", id));
        }
    }

    public static Optional<InfoArticleEntity> validateIdSearchResult(
            List<InfoArticleEntity> article, UUID id) {
        if (article.size() > 1) {
            throw new EntityToManyResultException(
                    String.format("More than one article found by id = %s", id));
        }
        if (article.isEmpty()) {
            return Optional.empty();
        }
        return article.stream().findFirst();
    }
}
