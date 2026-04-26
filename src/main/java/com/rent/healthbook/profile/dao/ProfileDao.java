package com.rent.healthbook.profile.dao;


import com.rent.healthbook.web.Pageable;
import com.rent.healthbook.profile.dto.enums.ProfileRole;
import com.rent.healthbook.profile.entity.ProfileEntity;
import com.rent.healthbook.profile.entity.ProfilesEntity;
import com.rent.healthbook.profile.service.ProfileSearchFilter;
import com.rent.healthbook.profile.service.ProfileValidator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfileDao {

    private final NamedParameterJdbcTemplate template;
    private final ProfileRowMapper rowMapper;

    public ProfileDao(NamedParameterJdbcTemplate template,
                      ProfileRowMapper rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }


    public Optional<ProfileEntity> getProfileByPhone(String phone) {
        String sqlLock = "SELECT * FROM profile WHERE phone = :phone";
        List<ProfileEntity> profile =
                template.query(sqlLock,
                        new MapSqlParameterSource("phone", phone),
                        rowMapper);
        return ProfileValidator.validatePhoneSearchResult(profile, phone);
    }


    public Optional<ProfileEntity> getProfileByPhoneWithLock(String phone) {
        String sqlLock = "SELECT * FROM profile WHERE phone = :phone FOR UPDATE";
        List<ProfileEntity> profile =
                template.query(sqlLock,
                        new MapSqlParameterSource("phone", phone),
                        rowMapper);
        return ProfileValidator.validatePhoneSearchResult(profile, phone);
    }


    public Boolean createProfile(ProfileEntity profile) {
        String sql = """
                INSERT INTO profile(id, first_name, last_name,
                                    patronymic, email,  password,
                                    phone, registration_date, is_fired,
                                     role)
                VALUES (:id, :firstName, :lastName,
                        :patronymic, :email, :password,
                        :phone, :registrationDate, :isFired, :role)
                """;
        var map = new MapSqlParameterSource()
                .addValue("id", UUID.randomUUID())
                .addValue("firstName", profile.getFirstName())
                .addValue("lastName", profile.getLastName())
                .addValue("patronymic", profile.getPatronymic())
                .addValue("email", profile.getEmail())
                .addValue("password", profile.getPassword())
                .addValue("phone", profile.getPhone())
                .addValue("registrationDate", LocalDate.now())
                .addValue("isFired", Boolean.TRUE)
                .addValue("role", ProfileRole.USER.name());

        return template.update(sql, map) == 1;
    }

    public Optional<ProfileEntity> getById(UUID id) {
        String sqlLock = "SELECT * FROM profile WHERE id = :id";
        List<ProfileEntity> profile =
                template.query(sqlLock,
                        new MapSqlParameterSource("id", id),
                        rowMapper);
        return ProfileValidator.validateIdSearchResult(profile, id);
    }


    public Optional<ProfileEntity> getByIdWithLock(UUID id) {
        String sqlLock = "SELECT * FROM profile WHERE id = :id FOR UPDATE";
        List<ProfileEntity> profile =
                template.query(sqlLock,
                        new MapSqlParameterSource("id", id),
                        rowMapper);
        return ProfileValidator.validateIdSearchResult(profile, id);
    }


    public ProfilesEntity getAllByFilter(ProfileSearchFilter filter,
                                         Pageable pageable) {
        String sql = """
        SELECT * FROM profile AS p
        WHERE  (p.email = COALESCE(:email, p.email))
                  AND (p.last_name = COALESCE(:lastName, p.last_name))
                  AND (p.first_name = COALESCE(:firstName, p.first_name))
                  AND (p.patronymic = COALESCE(:patronymic, p.patronymic) OR
                        (p.patronymic IS NOT DISTINCT FROM :patronymic))
                LIMIT :limit OFFSET :offset
        """;
        var map = new MapSqlParameterSource()
                .addValue("email", filter.email())
                .addValue("lastName", filter.lastName())
                .addValue("firstName", filter.firstName())
                .addValue("patronymic", filter.patronymic())
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getPageSize() * (pageable.getPageNumber()  - 1));
        return new ProfilesEntity(template.query(sql, map, rowMapper));
    }


    public boolean delete(UUID id) {
        String sql = "DELETE FROM profile WHERE id=:id";
        return 1 == template.update(sql, new MapSqlParameterSource("id", id));
    }


    public boolean update(ProfileEntity profile) {
        String sql = """
                UPDATE profile
                SET last_name=:lastName, first_name=:firstName,
                patronymic=:patronymic, email=:email, phone=:phone
                WHERE id=:id
                """;
        var map = new MapSqlParameterSource()
                .addValue("id", profile.getId())
                .addValue("firstName", profile.getFirstName())
                .addValue("lastName", profile.getLastName())
                .addValue("patronymic", profile.getPatronymic())
                .addValue("email", profile.getEmail())
                .addValue("phone", profile.getPhone());
        return 1 == template.update(sql, map);
    }


    public boolean updatePassword(UUID id, String password) {
        String sql = """
                UPDATE profile
                SET password =:password
                WHERE id = :id
                """;
        var map = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("password", password);
        return 1 == template.update(sql, map);
    }

    public boolean block(UUID id) {
        String sql = """
                UPDATE profile
                SET is_fired = false
                WHERE id=:id
                """;
        return 1 == template.update(sql, new MapSqlParameterSource("id", id));
    }

    public boolean existsById(UUID id) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM profile
                    WHERE id=:id
                )
                """;
        var map = new MapSqlParameterSource()
                .addValue("id", id);
        return Boolean.TRUE.equals(template.queryForObject(sql, map, Boolean.class));
    }
}
