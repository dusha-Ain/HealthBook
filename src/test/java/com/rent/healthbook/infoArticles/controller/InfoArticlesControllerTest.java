package com.rent.healthbook.infoArticles.controller;

import com.rent.healthbook.infoArticles.dto.InfoArticle;
import com.rent.healthbook.infoArticles.dto.InfoArticles;
import com.rent.healthbook.infoArticles.service.InfoArticlesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InfoArticlesControllerTest {

    @Mock
    private InfoArticlesService infoArticlesService;

    @InjectMocks
    private InfoArticlesController infoArticlesController;

    @Test
    void addArticle_ShouldReturnCreated() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        InfoArticle inputArticle = new InfoArticle(
                null, "How to Register", "Step by step guide",
                null, null, profileId);
        InfoArticle savedArticle = new InfoArticle(
                id, "How to Register", "Step by step guide",
                new Date(System.currentTimeMillis()), false, profileId);

        when(infoArticlesService.addArticle(any(InfoArticle.class))).thenReturn(savedArticle);

        ResponseEntity<InfoArticle> response = infoArticlesController.addArticle(inputArticle);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().id());
        verify(infoArticlesService, times(1)).addArticle(any(InfoArticle.class));
    }

    @Test
    void getArticleById_ShouldReturnArticle() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        InfoArticle article = new InfoArticle(
                id, "How to Register", "Step by step guide",
                new Date(System.currentTimeMillis()), false, profileId);

        when(infoArticlesService.getArticleById(id)).thenReturn(article);

        ResponseEntity<InfoArticle> response = infoArticlesController.getArticleById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().id());
        verify(infoArticlesService, times(1)).getArticleById(id);
    }

    @Test
    void getAllArticlesByFilter_ShouldReturnArticles() {
        InfoArticles mockArticles = new InfoArticles(java.util.Collections.emptyList());

        when(infoArticlesService.getAllDoctorsByFilter(any(com.rent.healthbook.web.Pageable.class)))
                .thenReturn(mockArticles);

        ResponseEntity<InfoArticles> response = infoArticlesController.getAllArticlesByFilter(10, 0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(infoArticlesService, times(1)).getAllDoctorsByFilter(any(com.rent.healthbook.web.Pageable.class));
    }

    @Test
    void updateArticle_ShouldReturnUpdatedArticle() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        InfoArticle updateData = new InfoArticle(
                id, "Updated Title", "Updated content",
                null, null, profileId);
        InfoArticle updatedArticle = new InfoArticle(
                id, "Updated Title", "Updated content",
                new Date(System.currentTimeMillis()), true, profileId);

        when(infoArticlesService.updateArticle(eq(id), any(InfoArticle.class))).thenReturn(updatedArticle);

        ResponseEntity<InfoArticle> response = infoArticlesController.updateArticle(id, updateData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(infoArticlesService, times(1)).updateArticle(eq(id), any(InfoArticle.class));
    }

    @Test
    void updateStatusArticle_ShouldReturnOk() {
        UUID id = UUID.randomUUID();
        Boolean status = true;

        doNothing().when(infoArticlesService).updateStatusArticle(eq(id), eq(status));

        ResponseEntity<Void> response = infoArticlesController.updateStatusArticle(id, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(infoArticlesService, times(1)).updateStatusArticle(eq(id), eq(status));
    }

    @Test
    void deleteArticle_ShouldReturnNoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(infoArticlesService).deleteArticle(id);

        ResponseEntity<Void> response = infoArticlesController.deleteArticle(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(infoArticlesService, times(1)).deleteArticle(id);
    }

    @Test
    void getArticleById_WhenNotFound_ShouldThrowException() {
        UUID nonExistentId = UUID.randomUUID();

        when(infoArticlesService.getArticleById(nonExistentId))
                .thenThrow(new RuntimeException("Article not found"));

        assertThrows(RuntimeException.class, () -> {
            infoArticlesController.getArticleById(nonExistentId);
        });
    }

    @Test
    void deleteArticle_WhenServiceThrowsException_ShouldPropagate() {
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException("Database error"))
                .when(infoArticlesService).deleteArticle(id);

        assertThrows(RuntimeException.class, () -> {
            infoArticlesController.deleteArticle(id);
        });
    }
}