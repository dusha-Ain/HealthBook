package com.rent.healthbook.infoArticles.service;

import com.rent.healthbook.infoArticles.dto.InfoArticle;
import com.rent.healthbook.infoArticles.entity.InfoArticleEntity;
import com.rent.healthbook.web.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InfoArticleMapperTest {

    private InfoArticleMapper mapper = new InfoArticleMapper();

    @Test
    void toEntity_FromInfoArticle_ShouldConvert() {
        UUID profileId = UUID.randomUUID();
        InfoArticle article = new InfoArticle(
                null,
                "How to Register",
                "Step by step guide",
                null,
                null,
                profileId);

        InfoArticleEntity entity = mapper.toEntity(article);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("How to Register", entity.getTitle());
        assertEquals("Step by step guide", entity.getContent());
        assertEquals(profileId, entity.getIdProfile());
    }

    @Test
    void toEntity_FromInfoArticle_ShouldThrowException_WhenNull() {
        assertThrows(ValidationException.class, () -> mapper.toEntity((InfoArticle) null));
    }

    @Test
    void toEntity_WithUpdate_ShouldUpdateExistingEntity() {
        InfoArticleEntity existingEntity = new InfoArticleEntity();
        existingEntity.setTitle("Old Title");
        existingEntity.setContent("Old Content");

        UUID profileId = UUID.randomUUID();
        InfoArticle update = new InfoArticle(
                null,
                "New Title",
                "New Content",
                null,
                null,
                profileId);

        InfoArticleEntity result = mapper.toEntity(existingEntity, update);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Content", result.getContent());
        assertEquals(profileId, result.getIdProfile());
    }

    @Test
    void toEntity_WithUpdate_ShouldReturnOriginal_WhenUpdateIsNull() {
        InfoArticleEntity existingEntity = new InfoArticleEntity();
        existingEntity.setTitle("Original Title");

        InfoArticleEntity result = mapper.toEntity(existingEntity, null);

        assertEquals(existingEntity, result);
        assertEquals("Original Title", result.getTitle());
    }

    @Test
    void toEntity_WithUpdate_ShouldOnlyUpdateNonNullFields() {
        InfoArticleEntity existingEntity = new InfoArticleEntity();
        existingEntity.setTitle("Old Title");
        existingEntity.setContent("Old Content");

        InfoArticle update = new InfoArticle(
                null,
                "New Title",  // only update title
                null,        // content not updated
                null,
                null,
                null);       // idProfile not updated

        InfoArticleEntity result = mapper.toEntity(existingEntity, update);

        assertEquals("New Title", result.getTitle());
        assertEquals("Old Content", result.getContent());
    }

    @Test
    void toDomainInfoArticle_ShouldConvertEntityToInfoArticle() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        Date created = new Date(System.currentTimeMillis());

        InfoArticleEntity entity = new InfoArticleEntity();
        entity.setId(id);
        entity.setTitle("How to Register");
        entity.setContent("Step by step guide");
        entity.setCreated(created);
        entity.setPublished(true);
        entity.setIdProfile(profileId);

        InfoArticle article = mapper.toDomainInfoArticle(entity);

        assertNotNull(article);
        assertEquals(id, article.id());
        assertEquals("How to Register", article.title());
        assertEquals("Step by step guide", article.content());
        assertEquals(created, article.created());
        assertTrue(article.isPublished());
        assertEquals(profileId, article.idProfile());
    }

    @Test
    void toDomainInfoArticle_ShouldThrowException_WhenEntityIsNull() {
        assertThrows(ValidationException.class, () -> mapper.toDomainInfoArticle(null));
    }
}