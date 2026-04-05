package com.rent.healthbook.consultant.dao;

import com.rent.healthbook.consultant.entity.ConsultantEntity;
import com.rent.healthbook.consultant.entity.ConsultantsEntity;
import com.rent.healthbook.consultant.service.ConsultantValidator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ConsultantDao {

    private final NamedParameterJdbcTemplate template;
    private final ConsultantRowMapper rowMapper;
    private final ConsultantValidator consultantValidator;

    public ConsultantDao(NamedParameterJdbcTemplate template,
                         ConsultantRowMapper rowMapper,
                         ConsultantValidator consultantValidator) {
        this.template = template;
        this.rowMapper = rowMapper;
        this.consultantValidator = consultantValidator;
    }

    public Optional<ConsultantEntity> getConsultantByProfileIdWithLock(UUID id) {
        String sqlLock = "SELECT * FROM consultant_profile WHERE id_profile = :idProfile FOR UPDATE";
        List<ConsultantEntity> consultant =
                template.query(sqlLock,
                        new MapSqlParameterSource("idProfile", id),
                        rowMapper);
        return consultantValidator.validateProfileIdSearchResult(consultant, id);
    }

    public Optional<ConsultantEntity> getConsultantByProfileId(UUID id) {
        String sql = "SELECT * FROM consultant_profile WHERE id_profile = :idProfile";
        List<ConsultantEntity> consultant =
                template.query(sql,
                        new MapSqlParameterSource("idProfile", id),
                        rowMapper);
        return consultantValidator.validateProfileIdSearchResult(consultant, id);
    }


    public Optional<ConsultantEntity> getConsultantByIdWithLock(UUID id) {
        String sqlLock = "SELECT * FROM consultant_profile WHERE id = :id FOR UPDATE";
        List<ConsultantEntity> consultant =
                template.query(sqlLock,
                        new MapSqlParameterSource("id", id),
                        rowMapper);
        return consultantValidator.validateIdSearchResult(consultant, id);
    }

    public Optional<ConsultantEntity> getConsultantById(UUID id) {
        String sqlLock = "SELECT * FROM consultant_profile WHERE id = :id";
        List<ConsultantEntity> consultant =
                template.query(sqlLock,
                        new MapSqlParameterSource("id", id),
                        rowMapper);
        return consultantValidator.validateIdSearchResult(consultant, id);
    }


    public Boolean createConsultant(ConsultantEntity consultant){
        String sql = """
                INSERT INTO consultant_profile(id, is_online, id_profile)
                VALUES(:id, :isOnline, :idProfile)
                """;
        var map = new MapSqlParameterSource()
                .addValue("id", UUID.randomUUID())
                .addValue("isOnline", Boolean.TRUE)
                .addValue("idProfile", consultant.getIdProfile());
        return 1 == template.update(sql, map);
    }

    public Boolean updateOnlineStatus(Boolean bool, UUID id){
        String sql = "UPDATE consultant_profile SET is_online=:bool WHERE id=:id";
        var map = new MapSqlParameterSource()
                .addValue("bool", bool)
                .addValue("id", id);
        return 1 == template.update(sql, map);
    }


    public ConsultantsEntity getAll() {
        String sql = "SELECT * FROM consultant_profile";
        return new ConsultantsEntity(template.query(sql, rowMapper));
    }


    public boolean delete(UUID id) {
        String sql = "DELETE FROM consultant_profile WHERE id=:id";
        return 1 == template.update(sql, new MapSqlParameterSource("id", id));
    }
}
