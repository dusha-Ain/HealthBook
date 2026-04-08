package com.rent.healthbook.consultant.dao;

import com.rent.healthbook.consultant.entity.ConsultantEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ConsultantRowMapper implements RowMapper<ConsultantEntity> {
    @Override
    public ConsultantEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        var consultant = new ConsultantEntity();
        consultant.setId(UUID.fromString(rs.getString("id")));
        consultant.setOnline(rs.getBoolean("is_online"));
        consultant.setIdProfile(UUID.fromString(rs.getString("id_profile")));
        return consultant;
    }
}