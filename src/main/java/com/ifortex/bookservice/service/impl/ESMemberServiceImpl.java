package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.repository.MemberRepository;
import com.ifortex.bookservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Attention! It is FORBIDDEN to make any changes in this file!
@Service
@RequiredArgsConstructor
public class ESMemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  @Override
  public Member findMember() {
    // will be implemented shortly
    return memberRepository.getMemberThatReadOldestRomanceBook();
  }

  @Override
  public List<Member> findMembers() {
    // will be implemented shortly
    return memberRepository.findMembersRegisteredIn2023();
  }
}
