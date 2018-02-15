package com.pjeng.memberadminservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.Repository;

import com.pjeng.memberadminservice.model.Member;

public interface MemberRepository extends Repository<Member, String> {
	public List<Member> findAll();

	public Member save(Member member);

	public void delete(Member member);

	public Member findById(String id);

	public List<Member> findByNameContainingIgnoreCase(String partialName);
}

