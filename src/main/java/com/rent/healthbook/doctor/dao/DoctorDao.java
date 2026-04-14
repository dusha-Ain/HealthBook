package com.rent.healthbook.doctor.dao;

import com.rent.healthbook.doctor.entity.DoctorEntity;
import com.rent.healthbook.doctor.entity.DoctorsEntity;
import com.rent.healthbook.doctor.service.DoctorValidator;
import com.rent.healthbook.web.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DoctorDao {

    private final NamedParameterJdbcTemplate template;
    private final DoctorRowMapper rowMapper;

    public DoctorDao(NamedParameterJdbcTemplate template,
                     DoctorRowMapper rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }

    public Optional<DoctorEntity> getDoctorByProfileIdWithLock(UUID id) {
        String sqlLock = "SELECT * FROM doctor_profile WHERE id_profile = :idProfile FOR UPDATE";
        List<DoctorEntity> doctor =
                template.query(sqlLock,
                        new MapSqlParameterSource("idProfile", id),
                        rowMapper);
        return DoctorValidator.validateProfileIdSearchResult(doctor, id);
    }

    public Optional<DoctorEntity> getDoctorByProfileId(UUID id) {
        String sqlLock = "SELECT * FROM doctor_profile WHERE id_profile = :idProfile";
        List<DoctorEntity> doctor =
                template.query(sqlLock,
                        new MapSqlParameterSource("idProfile", id),
                        rowMapper);
        return DoctorValidator.validateProfileIdSearchResult(doctor, id);
    }

    public Optional<DoctorEntity> getDoctorByIdWithLock(UUID id) {
        String sqlLock = "SELECT * FROM doctor_profile WHERE id = :id FOR UPDATE";
        List<DoctorEntity> doctor =
                template.query(sqlLock,
                        new MapSqlParameterSource("id", id),
                        rowMapper);
        return DoctorValidator.validateIdSearchResult(doctor, id);
    }

    public Optional<DoctorEntity> getDoctorById(UUID id) {
        String sqlLock = "SELECT * FROM doctor_profile WHERE id = :id";
        List<DoctorEntity> doctor =
                template.query(sqlLock,
                        new MapSqlParameterSource("id", id),
                        rowMapper);
        return DoctorValidator.validateIdSearchResult(doctor, id);
    }

    public Boolean createDoctor(DoctorEntity doctor){
        String sql = """
                INSERT INTO doctor_profile(id, specialization, cabinet_number,
                work_start, work_end, duration_reception, id_profile)
                VALUES(:id, :spec, :cabinet, :start, :end, :duration, :idProfile)
                """;
        var map = new MapSqlParameterSource()
                .addValue("id", UUID.randomUUID())
                .addValue("spec", doctor.getSpecialization())
                .addValue("cabinet", doctor.getCabinetNumber())
                .addValue("start", doctor.getWorkStart())
                .addValue("end", doctor.getWorkEnd())
                .addValue("duration", doctor.getDurationReception())
                .addValue("idProfile", doctor.getIdProfile());
        return  1 == template.update(sql, map);
    }

    public Boolean updateDoctor(DoctorEntity doctor){
        String sql = """
                UPDATE doctor_profile
                SET specialization=:spec, cabinet_number=:cabinet,
                work_start=:start, work_end=:end, duration_reception=:duration,
                id_profile=:idProfile
                WHERE id=:id
                """;
        var map = new MapSqlParameterSource()
                .addValue("spec", doctor.getSpecialization())
                .addValue("cabinet", doctor.getCabinetNumber())
                .addValue("start", doctor.getWorkStart())
                .addValue("end", doctor.getWorkEnd())
                .addValue("duration", doctor.getDurationReception())
                .addValue("idProfile", doctor.getIdProfile())
                .addValue("id", doctor.getId());
        return  1 == template.update(sql, map);
    }

    public DoctorsEntity getAllByFilter(Pageable filter) {
        String sql = "SELECT * FROM doctor_profile LIMIT :limit OFFSET :offset";
        var map = new MapSqlParameterSource()
                .addValue("limit", filter.getPageSize())
                .addValue("offset", filter.getPageSize() * (filter.getPageNumber() - 1));
        return new DoctorsEntity(template.query(sql, map, rowMapper));
    }

    public boolean deleteDoctor(UUID id){
        String sql = "DELETE FROM doctor_profile WHERE id=:id";
        return 1 == template.update(sql, new MapSqlParameterSource("id", id));
    }
}
