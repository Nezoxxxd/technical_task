package com.ifortex.bookservice.repository.impl;

import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.repository.MemberRepository;
import com.ifortex.bookservice.repository.mappers.BookRowMapper;
import com.ifortex.bookservice.repository.mappers.MemberRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final DataSource dataSourceConfig;

    private final String GET_MEMBER_THAT_READ_OLDEST_ROMANCE_BOOK_QUERY = """
            SELECT * FROM book_service.members mem
            INNER JOIN book_service.member_books mb
            ON mem.id = mb.member_id
            INNER JOIN book_service.books b
            ON mb.book_id = b.id AND 'Romance' = ANY(b.genre)
            ORDER BY b.publication_date, mem.membership_date DESC
            LIMIT 1;
            """;

    private final String FIND_MEMBERS_REGISTERED_IN_2023_QUERY = """
            SELECT * FROM book_service.members mem
            WHERE mem.id NOT IN (
                SELECT mb.member_id
                FROM book_service.member_books mb
            )
            AND EXTRACT(YEAR FROM mem.membership_date) = 2023;
            """;

    private final String GET_ALL_MEMBER_BOOKS_QUERY = """
            SELECT * FROM book_service.books b
            INNER JOIN book_service.member_books mb
            ON mb.member_id = ? AND b.id = mb.book_id
            """;

    @Override
    public Member getMemberThatReadOldestRomanceBook() {
        Member member;

        try (Connection connection = dataSourceConfig.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet rs = statement.executeQuery(GET_MEMBER_THAT_READ_OLDEST_ROMANCE_BOOK_QUERY)) {
                member = MemberRowMapper.mapMember(rs);
            }

            if (member != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_MEMBER_BOOKS_QUERY)) {
                    preparedStatement.setLong(1, member.getId());
                    List<Book> books;
                    try(ResultSet resultSet = preparedStatement.executeQuery()) {
                        books = BookRowMapper.mapBooks(resultSet);
                    }
                    member.setBorrowedBooks(books);
                }
            }

            return member;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Member> findMembersRegisteredIn2023() {
        try (Connection connection = dataSourceConfig.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet rs = statement.executeQuery(FIND_MEMBERS_REGISTERED_IN_2023_QUERY)) {
                return MemberRowMapper.mapRows(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
