package com.pshc.util.security.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pshc.util.dto.UserRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	UserRepository repasitory;
	@Override
	public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
		 //TODO Auto-generated method stub
		log.info("uid = " + uid);
		return Optional.ofNullable(repasitory.findByUid(uid))
						.filter(m -> m!=null)
						.map(m -> new SecurityMember(m)).get();
	}

}
