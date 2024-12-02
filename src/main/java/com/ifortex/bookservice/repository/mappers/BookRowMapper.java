package com.ifortex.bookservice.repository.mappers;

import com.ifortex.bookservice.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BookRowMapper {

    public static Map<String, Long> mapRows(ResultSet rs) throws SQLException {
        Map<String, Long> map = new LinkedHashMap<>();

        while(rs.next()) {
            String genre = rs.getString("genres");
            Long count = rs.getLong("ctn");
            map.put(genre, count);
        }
        return map;
    }

    public static List<Book> mapBooks(ResultSet rs) throws SQLException {
        List<Book> books = new ArrayList<>();

        while(rs.next()) {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setDescription(rs.getString("description"));
            book.setAuthor(rs.getString("author"));
            book.setPublicationDate(rs.getObject("publication_date", LocalDateTime.class));

            String genres = rs.getString("genre");
            book.setGenres(
                    Arrays.stream(genres.substring(1,genres.length() - 1).split(","))
                            .collect(Collectors.toSet())
            );

            books.add(book);
        }
        return books;
    }
}
