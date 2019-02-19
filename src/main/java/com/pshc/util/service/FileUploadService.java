package com.pshc.util.service;

import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pshc.util.dto.PostsDto;
import com.pshc.util.dto.PostsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileUploadService {
	@Autowired
	private AwsService awsService;
	@Autowired
	private PostsDto postsDto;
	@Autowired
	private PostsRepository postsRepasitory;
	
	public void doWork(HttpServletRequest request, MultipartFile multiFile) {
		try {
			File convFile = new File(multiFile.getOriginalFilename());
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(multiFile.getBytes());
			
			fos.close();
			
			postsDto.setCategory(request.getParameter("category"));
			postsDto.setVersion(request.getParameter("version"));
			postsDto.setDistinction(request.getParameter("distinction"));
			postsDto.setFileName(convFile.getName());
			log.info(postsDto.toString());
			awsService.fileUpload(postsDto.getCategory().toLowerCase(), convFile, convFile.getName());
			
			postsRepasitory.save(postsDto.toEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
