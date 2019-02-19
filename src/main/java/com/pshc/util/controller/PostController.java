package com.pshc.util.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.pshc.util.dto.PostsDto;
import com.pshc.util.dto.PostsRepository;
import com.pshc.util.model.Posts;
import com.pshc.util.service.FileUploadService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Controller
public class PostController {

	private PostsRepository postsRepository;
	private PostsDto postsDto;
	private FileUploadService fileUpload;

	protected String getRemoteIp() {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = req.getRemoteAddr();
		}
		log.debug(req.getHeader("user-agent"));

		return ip;
	}

	protected String getClientInfo() {
		return "C:" + getRemoteIp() + ", Rq:";
	}

	@GetMapping("/posts")
	public String postsView(Model model,
			@PageableDefault(sort = { "id" }, direction = Direction.DESC, size = 10) Pageable pageable) {
		Page<Posts> postsList = postsRepository.findAll(pageable);
		model.addAttribute("postslist", postsList);
		return "heag";
	}

	@RequestMapping("/uploadfile")
	public String uploadFile(@RequestPart MultipartFile files, HttpServletRequest request) {
		log.info(getClientInfo() + " /uploadfile " + files.getOriginalFilename());

		if (!files.isEmpty()) {
			fileUpload.doWork(request, files);
			log.info("111111111");
		}
		return "redirect:/posts";
	}

	@PostMapping("/updatepost")
	public String updatePosts(PostsDto posts, HttpServletRequest request) {
		log.info("/updatepost " + posts.getCategory() + " " + posts.getDistinction() + " " + posts.getId());
		postsRepository.setDistinctionFor(posts.getDistinction(), Long.parseLong(posts.getId()));
		return "redirect:/posts";

	}
}
