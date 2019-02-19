package com.pshc.util.dto;

import org.springframework.stereotype.Component;

import com.pshc.util.model.Posts;

import lombok.Data;
@Component
@Data
public class PostsDto {
	private String id;
	private String category;
	private String version;
	private String distinction;
	private String fileName;
	
	public Posts toEntity() {
		return Posts.builder()
				.category(category)
				.version(version)
				.distinction(distinction)
				.fileName(fileName)
				.build();
	}
	public String toString() {
		return category.toLowerCase()+"          "+
				version+"          "+
				distinction+"          "+
				fileName;
	}
}
