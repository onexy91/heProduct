package com.pshc.util.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.pshc.util.model.Member;
import com.pshc.util.model.MemberRole;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
@Getter
@Setter
@Slf4j
public class SecurityMember extends User{
	
	private static final String ROLE_PREFIX = "ROLE_";
	private static final long serialVersionUID = 1L;
	
	public SecurityMember(Member member) {
		super(member.getUid(), member.getPassword(), makeGrantedAuthority(member.getRoles()));
		log.info("SecurityMember initial! " + makeGrantedAuthority(member.getRoles()));
		// TODO Auto-generated constructor stub
	}

	
	//public SecurityMember(Member member) {
		
	//	super(member.getUid(), member.getPassword());
		// TODO Auto-generated constructor stub
	//}

	private static List<GrantedAuthority> makeGrantedAuthority(List<MemberRole> roles) {
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		roles.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleName())));
		return list;
	}
	
}
