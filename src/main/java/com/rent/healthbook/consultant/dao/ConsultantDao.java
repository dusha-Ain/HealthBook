package com.rent.healthbook.consultant.dao;

import com.rent.healthbook.consultant.entity.ConsultantEntity;
import com.rent.healthbook.consultant.entity.ConsultantsEntity;
import com.rent.healthbook.consultant.service.ConsultantValidator;
import com.rent.healthbook.profile.dto.enums.ProfileRole;
import com.rent.healthbook.web.helpers.Pageable;
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

    public ConsultantDao(NamedParameterJdbcTemplate template,
                         ConsultantRowMapper rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }

    public Optional<ConsultantEntity> getConsultantByProfileIdWithLock(UUID id) {
        String sqlLock = "SELECT * FROM consultant_profile WHERE id_profile = :idProfile FOR UPDATE";
        List<ConsultantEntity> consultant =
                template.query(sqlLock,
                        new MapSqlParameterSource("idProfile", id),
                        rowMapper);
        return ConsultantValidator.validateProfileIdSearchResult(consultant, id);
    }

    public Optional<ConsultantEntity> getConsultantByProfileId(UUID id) {
        String sql = "SELECT * FROM consultant_profile WHERE id_profile = :idProfile";
        List<ConsultantEntity> consultant =
                template.query(sql,
                        new MapSqlParameterSource("idProfile", id),
                        rowMapper);
        return ConsultantValidator.validateProfileIdSearchResult(consultant, id);
    }


    public Optional<ConsultantEntity> getConsultantByIdWithLock(UUID id) {
        String sqlLock = "SELECT * FROM consultant_profile WHERE id = :id FOR UPDATE";
        List<ConsultantEntity> consultant =
                template.query(sqlLock,
                        new MapSqlParameterSource("id", id),
                        rowMapper);
        return ConsultantValidator.validateIdSearchResult(consultant, id);
    }

    public Optional<ConsultantEntity> getConsultantById(UUID id) {
        String sqlLock = "SELECT * FROM consultant_profile WHERE id = :id";
        List<ConsultantEntity> consultant =
                template.query(sqlLock,
                        new MapSqlParameterSource("id", id),
                        rowMapper);
        return ConsultantValidator.validateIdSearchResult(consultant, id);
    }


    public Boolean createConsultant(ConsultantEntity consultant) {
        String sql = """
                INSERT INTO consultant_profile(id, is_online, id_profile)
                VALUES(:id, :isOnline, :idProfile)
                """;
        String sqlProfile = """
                UPDATE profile
                SET role = :role
                WHERE id = :idProfile
                """;
        var map = new MapSqlParameterSource()
                .addValue("id", UUID.randomUUID())
                .addValue("isOnline", Boolean.TRUE)
                .addValue("idProfile", consultant.getIdProfile())
                .addValue("role", ProfileRole.CONSULTANT.name());
        template.update(sqlProfile, map);
        return 1 == template.update(sql, map);
    }

    public Boolean updateOnlineStatus(Boolean bool, UUID id) {
        String sql = "UPDATE consultant_profile SET is_online=:bool WHERE id=:id";
        var map = new MapSqlParameterSource()
                .addValue("bool", bool)
                .addValue("id", id);
        return 1 == template.update(sql, map);
    }


    public ConsultantsEntity getAllByFilter(Pageable filter) {
        String sql = "SELECT * FROM consultant_profile LIMIT :limit OFFSET :offset";
        var map = new MapSqlParameterSource()
                .addValue("limit", filter.getPageSize())
                .addValue("offset", filter.getPageSize() * (filter.getPageNumber() - 1));
        return new ConsultantsEntity(template.query(sql, map, rowMapper));
    }


    public boolean delete(UUID id) {
        String sql = "DELETE FROM consultant_profile WHERE id=:id";
        return 1 == template.update(sql, new MapSqlParameterSource("id", id));
    }
}
