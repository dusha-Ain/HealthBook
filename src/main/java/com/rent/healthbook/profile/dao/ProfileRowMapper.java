package com.rent.healthbook.profile.dao;


import com.rent.healthbook.profile.dto.enums.ProfileRole;
import com.rent.healthbook.profile.entity.ProfileEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


@Component
public class ProfileRowMapper implements RowMapper<ProfileEntity> {
    @Override
    public ProfileEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        var profile = new ProfileEntity();
        profile.setId(UUID.fromString(rs.getString("id")));
        profile.setLastName(rs.getString("last_name"));
        profile.setFirstName(rs.getString("first_name"));
        profile.setPatronymic(rs.getString("patronymic"));
        profile.setEmail(rs.getString("email"));
        profile.setPassword(rs.getString("password"));
        profile.setPhone(rs.getString("phone"));
        profile.setRegistrationDate(rs.getDate("registration_date"));
        profile.setFired(rs.getBoolean("is_fired"));
        profile.setProfileRole(ProfileRole.valueOf(rs.getString("role")));
        return profile;
    }
}
