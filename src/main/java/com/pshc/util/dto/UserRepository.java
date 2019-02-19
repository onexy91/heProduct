package com.pshc.util.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pshc.util.model.Member;

public interface UserRepository extends JpaRepository<Member, Long>{
	Member findByUid(String uid);

}
