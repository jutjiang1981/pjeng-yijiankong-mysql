package com.pjeng.memberadminservice.controller;

import java.util.List;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pjeng.memberadminservice.exception.MemberNotFoundException;
import com.pjeng.memberadminservice.model.Member;
import com.pjeng.memberadminservice.repository.MemberRepository;


@RestController
public class MemberController {

	@Autowired
	private MemberRepository memberRepository;

	@GetMapping("/members")
	public List<Member> getAll() {

		List<Member> members = memberRepository.findAll();
		if (members == null || members.size() == 0) {
			throw new MemberNotFoundException("no members found");
		}
		return members;
	}

	// Create a new account
	@PostMapping("/members")
	public ResponseEntity<Member> create(@Valid @RequestBody Member member) {
		// @Valid annotation makes sure that the request body is valid
		Member newMember = memberRepository.save(member);
		return ResponseEntity.ok(newMember);
	}

	@GetMapping("/members/{id}")
	public ResponseEntity<Member> byNumber(@PathVariable("id") String id) {

		Member member = memberRepository.findById(id);

		if (member == null) {
			throw new MemberNotFoundException("no such member:" + id);
		}
		return ResponseEntity.ok().body(member);
	}

	@GetMapping("/members/name/{name}")
	public List<Member> byName(@PathVariable("name") String partialName) {

		List<Member> members = memberRepository.findByNameContainingIgnoreCase(partialName);

		if (members == null || members.size() == 0) {
			throw new MemberNotFoundException("no members named " + partialName);
		}

		return members;

	}

	// Update an account
	@PutMapping("/members/{id}")
	public ResponseEntity<Member> update(@PathVariable("id") String id, @Valid @RequestBody Member memberDetails) {
		Member member = memberRepository.findById(id);
		if (member == null) {
			throw new MemberNotFoundException("no such member:" + id);
		}
		member.setName(memberDetails.getName());

		Member updatedMember = memberRepository.save(member);
		return ResponseEntity.ok(updatedMember);
	}

	// Delete an account
	@DeleteMapping("/members/{id}")
	public ResponseEntity<Member> delete(@PathVariable("id") String id) {
		Member member = memberRepository.findById(id);
		if (member == null) {
			throw new MemberNotFoundException("no such member:" + id);
		}
		
		memberRepository.delete(member);
		return ResponseEntity.ok().build();
	}
}
