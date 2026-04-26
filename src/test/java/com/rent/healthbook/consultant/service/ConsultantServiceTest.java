package com.rent.healthbook.consultant.service;

import com.rent.healthbook.consultant.dao.ConsultantDao;
import com.rent.healthbook.consultant.dto.Consultant;
import com.rent.healthbook.consultant.dto.Consultants;
import com.rent.healthbook.consultant.entity.ConsultantEntity;
import com.rent.healthbook.consultant.entity.ConsultantsEntity;
import com.rent.healthbook.web.Pageable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultantServiceTest {

    @Mock
    private ConsultantMapper consultantMapper;

    @Mock
    private ConsultantDao consultantDao;

    @InjectMocks
    private ConsultantService consultantService;

    @Test
    void addConsultant_ShouldCreateAndReturnConsultant() {
        // Given
        UUID profileId = UUID.randomUUID();
        Consultant inputConsultant = new Consultant(null, null, profileId);

        // Entity that will be created by mapper
        ConsultantEntity entityToSave = new ConsultantEntity();
        entityToSave.setId(null);
        entityToSave.setOnline(null);
        entityToSave.setIdProfile(profileId);

        // Entity after saving (with generated ID)
        UUID generatedId = UUID.randomUUID();
        ConsultantEntity savedEntity = new ConsultantEntity();
        savedEntity.setId(generatedId);
        savedEntity.setOnline(true);
        savedEntity.setIdProfile(profileId);

        // Expected output DTO
        Consultant expectedOutput = new Consultant(generatedId, true, profileId);

        // Mock the DAO calls
        when(consultantDao.getConsultantByProfileIdWithLock(profileId)).thenReturn(Optional.empty());

        // Mock the mapper to return the entity
        when(consultantMapper.toEntity(inputConsultant)).thenReturn(entityToSave);

        when(consultantDao.createConsultant(entityToSave)).thenReturn(true);
        when(consultantDao.getConsultantByProfileId(profileId)).thenReturn(Optional.of(savedEntity));
        when(consultantMapper.toDomainConsultant(savedEntity)).thenReturn(expectedOutput);

        // When
        Consultant result = consultantService.addConsultant(inputConsultant);

        // Then
        assertNotNull(result);
        assertEquals(generatedId, result.id());
        assertEquals(profileId, result.idProfile());
    }

    @Test
    void addConsultant_ShouldThrowException_WhenConsultantAlreadyExists() {
        // Given
        UUID profileId = UUID.randomUUID();
        Consultant inputConsultant = new Consultant(null, null, profileId);
        ConsultantEntity existingEntity = new ConsultantEntity();

        when(consultantDao.getConsultantByProfileIdWithLock(profileId)).thenReturn(Optional.of(existingEntity));

        // When & Then
        assertThrows(Exception.class, () -> consultantService.addConsultant(inputConsultant));
        verify(consultantDao, never()).createConsultant(any());
    }

    @Test
    void getById_ShouldReturnConsultant_WhenExists() {
        // Given
        UUID id = UUID.randomUUID();
        ConsultantEntity entity = new ConsultantEntity();
        entity.setId(id);
        entity.setOnline(true);
        entity.setIdProfile(UUID.randomUUID());
        Consultant expectedConsultant = new Consultant(id, true, entity.getIdProfile());

        when(consultantDao.getConsultantById(id)).thenReturn(Optional.of(entity));
        when(consultantMapper.toDomainConsultant(entity)).thenReturn(expectedConsultant);

        // When
        Consultant result = consultantService.getById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.id());
        verify(consultantDao, times(1)).getConsultantById(id);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        when(consultantDao.getConsultantById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(Exception.class, () -> consultantService.getById(id));
    }

    @Test
    void getAllConsultantsByFilter_ShouldReturnPaginatedResults() {
        // Given
        Pageable filter = new Pageable();
        filter.setPageSize(10);
        filter.setPageNumber(1);

        ConsultantEntity entity1 = new ConsultantEntity();
        ConsultantEntity entity2 = new ConsultantEntity();
        ConsultantsEntity consultantsEntity = new ConsultantsEntity(List.of(entity1, entity2));

        Consultant consultant1 = new Consultant(UUID.randomUUID(), true, UUID.randomUUID());
        Consultant consultant2 = new Consultant(UUID.randomUUID(), false, UUID.randomUUID());

        when(consultantDao.getAllByFilter(any(Pageable.class))).thenReturn(consultantsEntity);
        when(consultantMapper.toDomainConsultant(entity1)).thenReturn(consultant1);
        when(consultantMapper.toDomainConsultant(entity2)).thenReturn(consultant2);

        // When
        Consultants result = consultantService.getAllConsultantsByFilter(filter);

        // Then
        assertNotNull(result);
        assertEquals(2, result.list.size());
        verify(consultantDao, times(1)).getAllByFilter(any(Pageable.class));
    }

    @Test
    void updateConsultantOnline_ShouldUpdateStatus_WhenConsultantExists() {
        // Given
        UUID id = UUID.randomUUID();
        Boolean online = true;
        ConsultantEntity existingEntity = new ConsultantEntity();

        when(consultantDao.getConsultantByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(consultantDao.updateOnlineStatus(online, id)).thenReturn(true);

        // When
        consultantService.updateConsultantOnline(id, online);

        // Then
        verify(consultantDao, times(1)).updateOnlineStatus(online, id);
    }

    @Test
    void updateConsultantOnline_ShouldThrowException_WhenConsultantNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        Boolean online = true;
        when(consultantDao.getConsultantByIdWithLock(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(Exception.class, () -> consultantService.updateConsultantOnline(id, online));
        verify(consultantDao, never()).updateOnlineStatus(any(), any());
    }

    @Test
    void deleteConsultant_ShouldDelete_WhenConsultantExists() {
        // Given
        UUID id = UUID.randomUUID();
        ConsultantEntity existingEntity = new ConsultantEntity();

        when(consultantDao.getConsultantByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(consultantDao.delete(id)).thenReturn(true);

        // When
        consultantService.deleteConsultant(id);

        // Then
        verify(consultantDao, times(1)).delete(id);
    }

    @Test
    void deleteConsultant_ShouldThrowException_WhenConsultantNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        when(consultantDao.getConsultantByIdWithLock(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(Exception.class, () -> consultantService.deleteConsultant(id));
        verify(consultantDao, never()).delete(any());
    }
}