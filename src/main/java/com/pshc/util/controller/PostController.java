package com.pshc.util.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pshc.util.dto.PostsDto;
import com.pshc.util.dto.PostsRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Controller 
public class PostController {
	private PostsRepository postRepository;
	private PostsDto postsDto;
	
	
	@PostMapping("/updatepost")
	public String updatePosts(PostsDto posts, HttpServletRequest request) {
		log.info("/updatepost "+ posts.getCategory()+" "+ posts.getDistinction()+ " "+ posts.getId() );
		postRepository.setDistinctionFor(posts.getDistinction(),  Long.parseLong(posts.getId()));
		return "redirect:/posts";
		
	}
}
