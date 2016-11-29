/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sg.cutepuppies.models.Content;
import com.sg.cutepuppies.models.Post;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import com.sg.cutepuppies.daos.CategoryDaoInterface;
import com.sg.cutepuppies.daos.ContentDaoInterface;
import com.sg.cutepuppies.daos.PostDaoInterface;
import com.sg.cutepuppies.daos.TagDaoInterface;
import com.sg.cutepuppies.daos.UserDaoInterface;
import com.sg.cutepuppies.models.User;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 *
 * @author apprentice
 */
public class ContentDAOTest {

    private PostDaoInterface postDao;
    private ContentDaoInterface contentDao;
    private CategoryDaoInterface categoryDao;
    private TagDaoInterface tagDao;
    private UserDaoInterface userDao;

    public ContentDAOTest() {

    }

    @Before
    public void setUp() {
        ApplicationContext ctx
                = new ClassPathXmlApplicationContext("test-applicationContext.xml");
        postDao = ctx.getBean("PostDBImplTest", PostDaoInterface.class);
        contentDao = ctx.getBean("ContentDBImplTest", ContentDaoInterface.class);
        categoryDao = ctx.getBean("CategoryDBImplTest", CategoryDaoInterface.class);
        tagDao = ctx.getBean("TagDBImplTest", TagDaoInterface.class);
        userDao = ctx.getBean("UserDBImplTest", UserDaoInterface.class);
        JdbcTemplate template = (JdbcTemplate) ctx.getBean("jdbcTemplate");

    }

    @Test
    public void testAddPostContent() {
        String date = "2000-11-01";
        java.sql.Date adminCreateDate = java.sql.Date.valueOf(date);

        String date2 = "2016-11-01";
        java.sql.Date contentCreateDate = java.sql.Date.valueOf(date2);
        
        User admin = new User();
        admin.setUserId(1);
        admin.setRoleCode("ADMIN");
        admin.setCreatedDate(adminCreateDate);
        admin.setUserName("sadukie");

        Content content = new Content();
        content.setPostId(1);
        content.setTitle("Title");
        content.setContentImgLink("Image Link");
        content.setContentImgAltTxt("Image Text");
        content.setBody("Body");
        content.setSnippet("Snippet");
        content.setContentTypeCode("POST");
        content.setCreatedByUser(admin);
        content.setCreatedOnDate(contentCreateDate);
        content.setUrlPattern("URL Pattern");
        content.setContentStatusCode("PUBLISHED");

        // int numRevisions = contentDAO.getContentByPostID(0).size();
        assertEquals(0, content.getContentId());

        contentDao.updatePostContent(content);

        // assertEquals(numRevisions + 1, contentDAO.getContentByPostID(0).size());
        assertNotEquals(0, content.getContentId());
    }

    @Test
    public void testGetAllContentsByPostId() {
        String date = "2000-11-01";
        java.sql.Date adminCreateDate = java.sql.Date.valueOf(date);

        String date2 = "2016-11-01";
        java.sql.Date contentCreateDate = java.sql.Date.valueOf(date2);
        
        User admin = new User();
        admin.setUserId(1);
        admin.setRoleCode("ADMIN");
        admin.setCreatedDate(adminCreateDate);
        admin.setUserName("sadukie");

        Post post = new Post();
        post.setPostId(1);
        post.setCreatedByUser(admin);

        Content content1 = new Content();
        content1.setContentId(1);
        content1.setPostId(1);
        content1.setTitle("Content1");
        content1.setContentStatusCode("PUBLISHED");
        content1.setUrlPattern("someUrlForContent1");
        content1.setContentTypeCode("POST");
        content1.setCreatedByUser(admin);
        content1.setCreatedOnDate(contentCreateDate);

        Content content2 = new Content();
        content2.setContentId(2);
        content2.setPostId(1);
        content2.setTitle("Content1");
        content2.setContentStatusCode("ARCHIVED");
        content2.setUrlPattern("someUrlForContent2");
        content2.setContentTypeCode("POST");
        content2.setCreatedByUser(admin);
        content2.setCreatedOnDate(contentCreateDate);

    }

}
