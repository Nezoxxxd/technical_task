package com.ifortex.bookservice.repository;

import com.ifortex.bookservice.model.Member;

import java.util.List;

public interface MemberRepository {

    Member getMemberThatReadOldestRomanceBook();

    List<Member> findMembersRegisteredIn2023();
}
