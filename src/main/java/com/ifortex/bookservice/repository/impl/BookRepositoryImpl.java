package com.ifortex.bookservice.repository.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.repository.BookRepository;
import com.ifortex.bookservice.repository.DataSourceConfig;
import com.ifortex.bookservice.repository.mappers.BookRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final DataSourceConfig dataSourceConfig;

    private final String GET_COUNTS_OF_GENRES_BY_BOOKS_QUERY = """
            SELECT UNNEST(b.genre) AS genres, COUNT(b.genre) AS ctn
            FROM book_service.books b
            GROUP BY genres
            ORDER BY ctn DESC;
            """;

    @Override
    public Map<String, Long> getCountsOfGenresByBooks() {
        try(Connection connection = dataSourceConfig.getConnection();
            Statement statement = connection.createStatement()) {

            try(ResultSet resultSet = statement.executeQuery(GET_COUNTS_OF_GENRES_BY_BOOKS_QUERY)) {
                return BookRowMapper.mapRows(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> getAllBooksByCriteria(SearchCriteria searchCriteria) {
        List<Object> params = new ArrayList<>();
        final String GET_ALL_BOOKS_BY_CRITERIA_QUERY = createQuery(searchCriteria, params);

        try(Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BOOKS_BY_CRITERIA_QUERY)) {

            for(int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = preparedStatement.executeQuery()) {
                return BookRowMapper.mapBooks(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String createQuery(SearchCriteria searchCriteria, List<Object> parameters) {
        StringBuilder builder = new StringBuilder("SELECT * FROM book_service.books b WHERE 1=1 ");

        if(isNotEmpty(searchCriteria.getTitle())) {
            builder.append("AND b.title LIKE ? ");
            parameters.add("%" + searchCriteria.getTitle() + "%");
        }
        if(isNotEmpty(searchCriteria.getAuthor())) {
            builder.append("AND b.author LIKE ? ");
            parameters.add("%" + searchCriteria.getAuthor() + "%");
        }
        if(isNotEmpty(searchCriteria.getGenre())) {
            builder.append("AND ? = ANY(b.genre) ");
            parameters.add(searchCriteria.getGenre());
        }
        if(isNotEmpty(searchCriteria.getDescription())) {
            builder.append("AND b.description LIKE ? ");
            parameters.add("%" + searchCriteria.getDescription() + "%");
        }
        if(searchCriteria.getYear() != null) {
            builder.append("AND EXTRACT(YEAR FROM b.publication_date) = ? ");
            parameters.add(searchCriteria.getYear());
        }

        return builder.append("ORDER BY b.publication_date;").toString();
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}
