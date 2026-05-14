package com.rent.healthbook.infoArticles.dao;

import com.rent.healthbook.infoArticles.entity.InfoArticleEntity;
import com.rent.healthbook.infoArticles.entity.InfoArticlesEntity;
import com.rent.healthbook.infoArticles.service.InfoArticlesValidator;
import com.rent.healthbook.web.helpers.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InfoArticlesDao {

    private final NamedParameterJdbcTemplate template;
    private final InfoArticlesRowMapper rowMapper;

    public InfoArticlesDao(NamedParameterJdbcTemplate template,
                           InfoArticlesRowMapper rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }


    public Optional<InfoArticleEntity> getArticleById(UUID id) {
        String sqlLock = "SELECT * FROM info_articles WHERE id = :id";
        List<InfoArticleEntity> article =
                template.query(sqlLock,
                        new MapSqlParameterSource("id", id),
                        rowMapper);
        return InfoArticlesValidator.validateIdSearchResult(article, id);
    }

    public Optional<InfoArticleEntity> getArticleByIdWithLock(UUID id) {
        String sqlLock = "SELECT * FROM info_articles WHERE id = :id FOR UPDATE";
        List<InfoArticleEntity> article =
                template.query(sqlLock,
                        new MapSqlParameterSource("id", id),
                        rowMapper);
        return InfoArticlesValidator.validateIdSearchResult(article, id);
    }

    public UUID createArticle(InfoArticleEntity article) {
        String sql = """
                INSERT INTO info_articles(id, title, content,
                 created, is_published, id_profile)
                VALUES (:id, :title, :content, :created, :publish, :idProfile)
                """;
        UUID idCreated = UUID.randomUUID();
        var map = new MapSqlParameterSource()
                .addValue("id", idCreated)
                .addValue("title", article.getTitle())
                .addValue("content", article.getContent())
                .addValue("created", LocalDate.now())
                .addValue("publish", Boolean.FALSE)
                .addValue("idProfile", article.getIdProfile());
        return 1 == template.update(sql, map) ? idCreated : null;
    }

    public boolean updateArticle(InfoArticleEntity article) {
        String sql = """
                UPDATE info_articles
                SET title=:title, content=:content, created=:created,
                    id_profile=:idProfile
                WHERE id=:id
                """;
        var map = new MapSqlParameterSource()
                .addValue("id", article.getId())
                .addValue("title", article.getTitle())
                .addValue("content", article.getContent())
                .addValue("created", article.getCreated())
                .addValue("publish", article.getPublished())
                .addValue("idProfile", article.getIdProfile());
        return 1 == template.update(sql, map);
    }

    public boolean updateStatusArticle(UUID id, Boolean status){
        String sql = "UPDATE info_articles SET is_published=:publish WHERE id=:id";
        var map = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("publish", status);
        return 1 == template.update(sql, map);
    }

    public InfoArticlesEntity getAllByFilter(Pageable filter) {
        String sql = "SELECT * FROM info_articles LIMIT :limit OFFSET :offset";
        var map = new MapSqlParameterSource()
                .addValue("limit", filter.getPageSize())
                .addValue("offset", filter.getPageSize() * (filter.getPageNumber() - 1));
        return new InfoArticlesEntity(template.query(sql, map, rowMapper));
    }

    public boolean deleteArticle(UUID id){
        String sql = "DELETE FROM info_articles WHERE id=:id";
        return 1 == template.update(sql, new MapSqlParameterSource("id", id));
    }
}
