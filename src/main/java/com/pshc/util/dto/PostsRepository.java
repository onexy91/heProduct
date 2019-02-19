package com.pshc.util.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pshc.util.model.Posts;

public interface PostsRepository extends JpaRepository<Posts, Long>{
	
	List<Posts> findByDistinction(String distinction);
	@Transactional
	@Modifying
	@Query("update Posts p set p.distinction = ?1 where p.id = ?2") 
	int setDistinctionFor(@Param("distinction") String distinction, @Param("id") Long id);
	
}
