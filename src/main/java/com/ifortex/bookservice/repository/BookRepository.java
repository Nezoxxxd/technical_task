package com.ifortex.bookservice.repository;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;

import java.util.List;
import java.util.Map;

public interface BookRepository {

    Map<String, Long> getCountsOfGenresByBooks();

    List<Book> getAllBooksByCriteria(SearchCriteria searchCriteria);
}
