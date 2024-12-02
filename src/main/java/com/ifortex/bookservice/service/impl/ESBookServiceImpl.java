package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.repository.BookRepository;
import com.ifortex.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// Attention! It is FORBIDDEN to make any changes in this file!
@Service
@RequiredArgsConstructor
public class ESBookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  @Override
  public Map<String, Long> getBooks() {
    // will be implemented shortly
    return bookRepository.getCountsOfGenresByBooks();
  }

  @Override
  public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
    // will be implemented shortly
    return bookRepository.getAllBooksByCriteria(searchCriteria);
  }
}
