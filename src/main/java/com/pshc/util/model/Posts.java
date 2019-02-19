package com.pshc.util.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Posts {
	@GeneratedValue
	@Id
	private Long id;
	@Column
	private String category;
	@Column
	private String version;
	@Column
	private String distinction;
	@Column
	private String fileName;
	
	@Builder
	public Posts(String category, String version, String distinction, String fileName) {
		this.category = category;
		this.version = version;
		this.distinction = distinction;
		this.fileName = fileName;
	}
}
