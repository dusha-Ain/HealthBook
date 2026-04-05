package com.rent.healthbook.doctor.dao;

import com.rent.healthbook.doctor.entity.DoctorEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class DoctorRowMapper implements RowMapper<DoctorEntity> {
    @Override
    public DoctorEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        var doctor = new DoctorEntity();
        doctor.setId(UUID.fromString(rs.getString("id")));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setCabinetNumber(rs.getString("cabinet_number"));
        doctor.setWorkStart(rs.getDate("work_start"));
        doctor.setWorkEnd(rs.getDate("work_end"));
        doctor.setDurationReception(rs.getTime("duration_reception"));
        doctor.setIdProfile(UUID.fromString(rs.getString("id_profile")));
        return doctor;
    }
}
