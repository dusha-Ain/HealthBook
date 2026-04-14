package com.rent.healthbook.infoArticles.dao;

import com.rent.healthbook.infoArticles.entity.InfoArticleEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class InfoArticlesRowMapper implements RowMapper<InfoArticleEntity> {
    @Override
    public InfoArticleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        var article = new InfoArticleEntity();
        article.setId(UUID.fromString(rs.getString("id")));
        article.setTitle(rs.getString("title"));
        article.setContent(rs.getString("content"));
        article.setCreated(rs.getDate("created"));
        article.setPublished(rs.getBoolean("is_published"));
        article.setIdProfile(UUID.fromString(rs.getString("id_profile")));
        return article;
    }
}
