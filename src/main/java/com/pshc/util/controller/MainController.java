package com.pshc.util.controller;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.pshc.util.dto.PostsRepository;
import com.pshc.util.dto.UserRepository;
import com.pshc.util.model.FileCommand;
import com.pshc.util.model.Member;
import com.pshc.util.model.MemberRole;
import com.pshc.util.model.Posts;
import com.pshc.util.service.AwsService;
import com.pshc.util.service.FileUpload;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Controller
@Slf4j
public class MainController {

	private UserRepository userRepasitory;
	private PostsRepository postsRepasitory;
	private FileUpload fileUpload;
	private AwsService awsService;

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
	//로그인 view
	@RequestMapping("/")
	public String loginView() {
		return "login";
	}

	@GetMapping("/main")
	public String menuPageView(Model model) {
		log.info(getClientInfo() + "/main");

		// List<Posts> postList = postsRepasitory.findAll();
		List<Posts> postList = postsRepasitory.findByDistinction("정식");
		model.addAttribute("postslist", postList);

		return "index";
	}
	
	@RequestMapping("/login")
	public String loginForm(HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		request.getSession().setAttribute("prevPage", referer);
		return "login";
	}
	//회원가입 view 
	@RequestMapping("/sign")
	public String signUp() {

		return "sign";
	}
	
	//회원가입 Proc
	@PostMapping("/members")
	public String insert(Member member) {
		MemberRole role = new MemberRole();
		BCryptPasswordEncoder pEncoder = new BCryptPasswordEncoder();
		member.setPassword(pEncoder.encode(member.getPassword()));
		role.setRoleName("USER");
		member.setRoles(Arrays.asList(role));
		userRepasitory.save(member);
		return "redirect:/";

	}

	@GetMapping("/posts")
	public String postsView(Model model,
			@PageableDefault(sort = { "id" }, direction = Direction.DESC, size = 10) Pageable pageable) {
		Page<Posts> postsList = postsRepasitory.findAll(pageable);
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

	/*
	 * ajax 로 post방식 하려했으나 stream 처리불가 로 getMapping
	 */
	@GetMapping("/filedown")
	public void fileDown(FileCommand posts, HttpServletResponse response) {
		log.info(getClientInfo() + "/filedown?" + posts.getCategory() + " " + posts.getFileName());
		
		OutputStream responseOut = null;
		String bucketName = posts.getCategory();
		String fileName = posts.getFileName();

		try {
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			responseOut = response.getOutputStream();
			awsService.downloadFile(bucketName.toLowerCase(), fileName, responseOut);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		} finally {
			try {
				if (responseOut != null)
					responseOut.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

}
