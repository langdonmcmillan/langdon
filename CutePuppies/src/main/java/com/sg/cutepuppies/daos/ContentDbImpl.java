/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.cutepuppies.daos;

import com.sg.cutepuppies.models.Content;
import com.sg.cutepuppies.models.Post;
import com.sg.cutepuppies.models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author apprentice
 */
public class ContentDbImpl implements ContentDaoInterface {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate npJdbcTemplate;

    // setter injection
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.npJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
    }

    // SQL PREPARED STATEMENTS
    private static final String SQL_GET_ALL_REVISIONS_BY_POST_ID 
            = "select c.* from Content c "
            + " join Post p on c.PostId = p.PostId "
            + " where c.PostId = ?";
    private static final String SQL_GET_PUBLISHED_CONTENT_BY_POST_ID = "select c.* from Content c join Post p "
            + "on c.PostId = p.PostId where c.ContentStatusCode = 'PUBLISHED' and c.PostId = ?";
    
    private static final String SQL_GET_MOST_RECENT_CONTENT_BY_POST_ID 
            = " select c.* "
            + " from Content c "
            + " join Post p "
            + " on c.PostId = p.PostId"
            + " where p.PostId = ? "
            + " order by c.ContentId desc"
            + " limit 1";
    
    private static final String SQL_ADD_CONTENT_TO_POST = "insert into Content (PostId, Title, "
            + "ContentImgLink, ContentImgAltTxt, Body, Snippet, ContentStatusCode, UrlPattern, ContentTypeCode, "
            + "CreatedByUserId) "
            + "values (:postID, :title, :contentImgLink, :contentImgAltTxt, :body, :snippet, :contentStatusCode, "
            + ":urlPattern, :contentTypeCode, :createdByUserID)";
    private static final String SQL_ARCHIVE_OLD_CONTENT = "update Content set ContentStatusCode = 'ARCHIVED' "
            + "where ContentStatusCode = 'PUBLISHED' and postID = :postID";
    private static final String SQL_UPDATE_CONTENT_CATEGORIES = "insert into content_category (ContentId, CategoryId) "
            + "values (?, ?)";
    private static final String SQL_UPDATE_CONTENT_TAGS = "insert into content_tag (ContentId, TagId) "
            + "values (?, ?)";
    private static final String SQL_ARCHIVE_POST = "update Content set ArchivedByUserId = :userId, "
            + "UpdatedByUserId = :userId, ArchivedOnDate = Current_Timestamp, ContentStatusCode = 'ARCHIVED' where PostId = :postId";
    private static final String SQL_ARCHIVE_CONTENT = "update Content set ArchivedByUserId = :userId, "
            + "UpdatedByUserId = :userId, ArchivedOnDate = Current_Timestamp, ContentStatusCode = 'ARCHIVED' where ContentId = :contentId";
    
    private static final String SQL_ARCHIVE_CONTENT_BY_STATUS = "update Content set ContentStatusCode = 'ARCHIVED'"
            + "where ContentStatusCode =:contentStatusCode and postID = :postID";
    
    @Override
    public List<Content> getAllContentsByPostId(int postID) {
        return jdbcTemplate.query(SQL_GET_ALL_REVISIONS_BY_POST_ID, new ContentMapper(), postID);
    }

    @Override
    public Content updatePostContent(Content content) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        /*
        Calendar today = Calendar.getInstance();
        java.sql.Date currentDate = new java.sql.Date((today.getTime()).getTime());
        content.setCreatedOnDate(currentDate); */
        namedParameters.addValue("postID", content.getPostId());
        namedParameters.addValue("title", content.getTitle());
        namedParameters.addValue("contentImgLink", content.getContentImgLink());
        namedParameters.addValue("contentImgAltTxt", content.getContentImgAltTxt());
        namedParameters.addValue("body", content.getBody());
        namedParameters.addValue("snippet", content.getSnippet());
        namedParameters.addValue("contentStatusCode", content.getContentStatusCode());
        namedParameters.addValue("urlPattern", content.getUrlPattern());
        namedParameters.addValue("contentTypeCode", content.getContentTypeCode());
        namedParameters.addValue("createdByUserID", content.getCreatedByUser().getUserId());
        namedParameters.addValue("createdOnDate", content.getCreatedOnDate());
        
       archiveContentByStatus(content.getPostId(), content.getContentStatusCode());
        
        npJdbcTemplate.update(SQL_ADD_CONTENT_TO_POST, namedParameters);
        content.setContentId(jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class));
        updateContentCategories(content);
        updateContentTags(content);
        return content;
    }
    
     private void archiveContentByStatus(int postId, String contentStatusCode) {
        
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("postID", postId);
        namedParameters.addValue("contentStatusCode", contentStatusCode);
        npJdbcTemplate.update(SQL_ARCHIVE_CONTENT_BY_STATUS, namedParameters);
         
    }

    private void archiveOldContent(int postID) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("postID", postID);
        npJdbcTemplate.update(SQL_ARCHIVE_OLD_CONTENT, namedParameters);
    }

    private void updateContentCategories(Content content) {
        try {
            jdbcTemplate.batchUpdate(SQL_UPDATE_CONTENT_CATEGORIES, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, content.getContentId());
                    ps.setInt(2, content.getListOfCategories().get(i).getCategoryID());
                }

                @Override
                public int getBatchSize() {
                    return content.getListOfCategories().size();
                }
            });
        } catch (Exception e) {
        }
    }

    private void updateContentTags(Content content) {
        try {
            jdbcTemplate.batchUpdate(SQL_UPDATE_CONTENT_TAGS, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, content.getContentId());
                    ps.setInt(2, content.getListOfTags().get(i).getTagID());
                }

                @Override
                public int getBatchSize() {
                    return content.getListOfTags().size();
                }
            });
        } catch (Exception e) {
        }

    }

    @Override
    public List<Content> getAllStaticPages() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Content getStaticPageByURL(String urlPattern) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Content addStaticPage(Content content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Content updateStaticPage(Content content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Content getPublishedPostContent(int postID) {
        return jdbcTemplate.queryForObject(SQL_GET_PUBLISHED_CONTENT_BY_POST_ID, new ContentMapper(), postID);
    }

    @Override
    public Content getMostRecentPostContent(int postID) {
        return jdbcTemplate.queryForObject(SQL_GET_MOST_RECENT_CONTENT_BY_POST_ID, new ContentMapper(), postID);
    }

    @Override
    public void archiveContent(int contentId, int userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        namedParameters.addValue("contentId", contentId);
        npJdbcTemplate.update(SQL_ARCHIVE_CONTENT, namedParameters);
    }

    @Override
    public void archivePost(int postId, int userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        namedParameters.addValue("postId", postId);
        npJdbcTemplate.update(SQL_ARCHIVE_POST, namedParameters);
    }

   

    private static final class ContentMapper implements RowMapper<Content> {

        @Override
        public Content mapRow(ResultSet rs, int i) throws SQLException {

            Content content = new Content();
            content.setContentId(rs.getInt("ContentId"));
            content.setPostId(rs.getInt("PostId"));
            content.setTitle(rs.getString("Title"));
            content.setContentImgLink(rs.getString("ContentImgLink"));
            content.setContentImgAltTxt(rs.getString("ContentImgAltTxt"));
            content.setBody(rs.getString("Body"));
            content.setSnippet(rs.getString("Snippet"));
            content.setContentStatusCode(rs.getString("ContentStatusCode"));
            content.setUrlPattern(rs.getString("UrlPattern"));
            content.setContentTypeCode(rs.getString("ContentTypeCode"));
            content.setCreatedOnDate(rs.getDate("CreatedOnDate"));
            content.setUpdatedOnDate(rs.getDate("UpdatedOnDate"));
            content.setArchivedOnDate(rs.getDate("ArchivedOnDate"));
            
            return content;
        }
    }
}
