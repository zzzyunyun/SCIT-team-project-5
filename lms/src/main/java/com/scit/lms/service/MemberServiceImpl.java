package com.scit.lms.service;

import com.scit.lms.dao.MemberDAO;
import com.scit.lms.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDAO dao;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public int join(Member member){
		String pw = passwordEncoder.encode(member.getMemberpw());

		log.debug("암호화 전 : {}", member.getMemberpw());
		log.debug("암호화 후 : {}", pw);

		member.setMemberpw(pw);

		int n = dao.join(member);

		return n;
	}

	@Override
	public boolean idcheck(String searchid) {
		return dao.selectOne(searchid) == null;
	}
}
