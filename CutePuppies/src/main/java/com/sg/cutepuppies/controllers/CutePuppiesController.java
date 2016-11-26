/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.cutepuppies.controllers;

import com.sg.cutepuppies.daos.CategoryDAOInterface;
import com.sg.cutepuppies.daos.ContentDAOInterface;
import com.sg.cutepuppies.daos.PostDAOInterface;
import com.sg.cutepuppies.daos.TagDAOInterface;
import com.sg.cutepuppies.models.Content;
import com.sg.cutepuppies.models.Post;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author apprentice
 */
@Controller
public class CutePuppiesController {
    
    private CategoryDAOInterface categoryDao;
    private TagDAOInterface tagDao;
    private ContentDAOInterface contentDao;
    private PostDAOInterface postDao;

    @Inject
    public CutePuppiesController(CategoryDAOInterface categoryDao, TagDAOInterface tagDao, ContentDAOInterface contentDao, PostDAOInterface postDao) {
        this.categoryDao = categoryDao;
        this.tagDao = tagDao;
        this.contentDao = contentDao;
        this.postDao = postDao;
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String getLandingPage() {
        return "blog";
    }

    @RequestMapping(value = "/getAllPosts", method = RequestMethod.GET)
    @ResponseBody
    public List<Post> getAllPosts(String postsPerPage, String nextPage) {
        // get me a list of Post objects.
        List<Post> listOfPosts = postDao.getAllPosts();
        
        for (Post post : listOfPosts) {
            int postId = post.getPostId();
            Content contentPerPost = postDao.getPublishedContentOfPost(postId);
            post.setPublishedContent(contentPerPost);
        }
        return listOfPosts;
    }

}
