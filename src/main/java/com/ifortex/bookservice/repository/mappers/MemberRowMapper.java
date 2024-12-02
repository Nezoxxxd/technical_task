package com.ifortex.bookservice.repository.mappers;

import com.ifortex.bookservice.model.Member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemberRowMapper {

    public static Member mapMember(ResultSet rs) throws SQLException {
        if(rs.next()) {
            Member member = new Member();
            member.setId(rs.getInt("id"));
            member.setName(rs.getString("name"));
            member.setMembershipDate(rs.getObject("membership_date", LocalDateTime.class));

            return member;
        }
        return null;
    }

    public static List<Member> mapRows(ResultSet rs) throws SQLException {
        List<Member> members = new ArrayList<>();

        while(rs.next()) {
            Member member = new Member();
            member.setId(rs.getInt("id"));
            member.setName(rs.getString("name"));
            member.setMembershipDate(rs.getObject("membership_date", LocalDateTime.class));

            members.add(member);
        }
        return members;
    }
}
