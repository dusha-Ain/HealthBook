package com.rent.healthbook.infoArticles.service;

import com.rent.healthbook.infoArticles.dao.InfoArticlesDao;
import com.rent.healthbook.infoArticles.dto.InfoArticle;
import com.rent.healthbook.infoArticles.dto.InfoArticles;
import com.rent.healthbook.infoArticles.entity.InfoArticleEntity;
import com.rent.healthbook.infoArticles.entity.InfoArticlesEntity;
import com.rent.healthbook.web.helpers.Pageable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InfoArticlesServiceTest {

    @Mock
    private InfoArticleMapper infoArticleMapper;

    @Mock
    private InfoArticlesDao infoArticlesDao;

    @InjectMocks
    private InfoArticlesService infoArticlesService;

    @Test
    void addArticle_ShouldCreateAndReturnArticle() {
        UUID profileId = UUID.randomUUID();
        UUID generatedId = UUID.randomUUID();

        InfoArticle inputArticle = new InfoArticle(
                null, "How to Register", "Step by step guide",
                null, null, profileId);

        InfoArticleEntity entityToSave = new InfoArticleEntity();
        InfoArticleEntity savedEntity = new InfoArticleEntity();
        savedEntity.setId(generatedId);
        savedEntity.setTitle("How to Register");
        savedEntity.setContent("Step by step guide");
        savedEntity.setIdProfile(profileId);

        InfoArticle expectedOutput = new InfoArticle(
                generatedId, "How to Register", "Step by step guide",
                new Date(System.currentTimeMillis()), false, profileId);

        when(infoArticleMapper.toEntity(inputArticle)).thenReturn(entityToSave);
        when(infoArticlesDao.createArticle(entityToSave)).thenReturn(generatedId);
        when(infoArticlesDao.getArticleById(generatedId)).thenReturn(Optional.of(savedEntity));
        when(infoArticleMapper.toDomainInfoArticle(savedEntity)).thenReturn(expectedOutput);

        InfoArticle result = infoArticlesService.addArticle(inputArticle);

        assertNotNull(result);
        assertEquals(generatedId, result.id());
        verify(infoArticlesDao, times(1)).createArticle(entityToSave);
    }

    @Test
    void addArticle_ShouldThrowException_WhenCreationFails() {
        UUID profileId = UUID.randomUUID();
        InfoArticle inputArticle = new InfoArticle(
                null, "How to Register", "Step by step guide",
                null, null, profileId);

        InfoArticleEntity entityToSave = new InfoArticleEntity();

        when(infoArticleMapper.toEntity(inputArticle)).thenReturn(entityToSave);
        when(infoArticlesDao.createArticle(entityToSave)).thenReturn(null);

        assertThrows(Exception.class, () -> infoArticlesService.addArticle(inputArticle));
        verify(infoArticlesDao, never()).getArticleById(any());

    }

    @Test
    void getArticleById_ShouldReturnArticle_WhenExists() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        InfoArticleEntity entity = new InfoArticleEntity();
        entity.setId(id);
        entity.setTitle("How to Register");
        entity.setIdProfile(profileId);
        InfoArticle expectedArticle = new InfoArticle(
                id, "How to Register", null, null, null, profileId);

        when(infoArticlesDao.getArticleById(id)).thenReturn(Optional.of(entity));
        when(infoArticleMapper.toDomainInfoArticle(entity)).thenReturn(expectedArticle);

        InfoArticle result = infoArticlesService.getArticleById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(infoArticlesDao, times(1)).getArticleById(id);
    }

    @Test
    void getArticleById_ShouldThrowException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(infoArticlesDao.getArticleById(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> infoArticlesService.getArticleById(id));
    }

    @Test
    void getAllDoctorsByFilter_ShouldReturnPaginatedResults() {
        Pageable filter = new Pageable();
        filter.setPageSize(10);
        filter.setPageNumber(1);

        InfoArticleEntity entity1 = new InfoArticleEntity();
        InfoArticleEntity entity2 = new InfoArticleEntity();
        InfoArticlesEntity articlesEntity = new InfoArticlesEntity(List.of(entity1, entity2));

        InfoArticle article1 = new InfoArticle(UUID.randomUUID(), "Title1", "Content1", null, null, UUID.randomUUID());
        InfoArticle article2 = new InfoArticle(UUID.randomUUID(), "Title2", "Content2", null, null, UUID.randomUUID());

        when(infoArticlesDao.getAllByFilter(any(Pageable.class))).thenReturn(articlesEntity);
        when(infoArticleMapper.toDomainInfoArticle(entity1)).thenReturn(article1);
        when(infoArticleMapper.toDomainInfoArticle(entity2)).thenReturn(article2);

        InfoArticles result = infoArticlesService.getAllDoctorsByFilter(filter);

        assertNotNull(result);
        assertEquals(2, result.list.size());
        verify(infoArticlesDao, times(1)).getAllByFilter(any(Pageable.class));
    }

    @Test
    void updateArticle_ShouldUpdateAndReturnArticle() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        InfoArticle updateData = new InfoArticle(
                id, "Updated Title", "Updated content",
                null, null, profileId);

        InfoArticleEntity existingEntity = new InfoArticleEntity();
        existingEntity.setId(id);
        InfoArticleEntity updatedEntity = new InfoArticleEntity();
        updatedEntity.setId(id);
        updatedEntity.setTitle("Updated Title");
        InfoArticle expectedArticle = new InfoArticle(
                id, "Updated Title", "Updated content", null, null, profileId);

        when(infoArticlesDao.getArticleByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(infoArticleMapper.toEntity(existingEntity, updateData)).thenReturn(updatedEntity);
        when(infoArticlesDao.updateArticle(updatedEntity)).thenReturn(true);
        when(infoArticlesDao.getArticleById(id)).thenReturn(Optional.of(updatedEntity));
        when(infoArticleMapper.toDomainInfoArticle(updatedEntity)).thenReturn(expectedArticle);

        InfoArticle result = infoArticlesService.updateArticle(id, updateData);

        assertNotNull(result);
        assertEquals("Updated Title", result.title());
        verify(infoArticlesDao, times(1)).updateArticle(updatedEntity);
    }

    @Test
    void updateArticle_ShouldThrowException_WhenArticleNotFound() {
        UUID id = UUID.randomUUID();
        InfoArticle updateData = new InfoArticle(
                id, "Updated Title", "Updated content", null, null, UUID.randomUUID());

        when(infoArticlesDao.getArticleByIdWithLock(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> infoArticlesService.updateArticle(id, updateData));
        verify(infoArticlesDao, never()).updateArticle(any());
    }

    @Test
    void updateStatusArticle_ShouldUpdateStatus_WhenArticleExists() {
        UUID id = UUID.randomUUID();
        Boolean status = true;
        InfoArticleEntity existingEntity = new InfoArticleEntity();

        when(infoArticlesDao.getArticleByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(infoArticlesDao.updateStatusArticle(id, status)).thenReturn(true);

        infoArticlesService.updateStatusArticle(id, status);

        verify(infoArticlesDao, times(1)).updateStatusArticle(id, status);
    }

    @Test
    void updateStatusArticle_ShouldThrowException_WhenArticleNotFound() {
        UUID id = UUID.randomUUID();
        Boolean status = true;

        when(infoArticlesDao.getArticleByIdWithLock(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> infoArticlesService.updateStatusArticle(id, status));
        verify(infoArticlesDao, never()).updateStatusArticle(any(), any());
    }

    @Test
    void deleteArticle_ShouldDelete_WhenArticleExists() {
        UUID id = UUID.randomUUID();
        InfoArticleEntity existingEntity = new InfoArticleEntity();

        when(infoArticlesDao.getArticleByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(infoArticlesDao.deleteArticle(id)).thenReturn(true);

        infoArticlesService.deleteArticle(id);

        verify(infoArticlesDao, times(1)).deleteArticle(id);
    }

    @Test
    void deleteArticle_ShouldThrowException_WhenArticleNotFound() {
        UUID id = UUID.randomUUID();
        when(infoArticlesDao.getArticleByIdWithLock(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> infoArticlesService.deleteArticle(id));
        verify(infoArticlesDao, never()).deleteArticle(any());
    }
}