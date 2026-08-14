package com.example.book_search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponseDto> search(BookRequestDto req) {
        if (req.isEmpty())
            return List.of();

        Specification<Book> spec = BookSpecifications.keywordContains(req.keyword())
                .and(BookSpecifications.publisherEquals(req.publisher()))
                .and(BookSpecifications.priceMax(req.priceMax()))
                .and(BookSpecifications.yearFrom(req.yearFrom()))
                .and(BookSpecifications.yearTo(req.yearTo()));

        List<BookResponseDto> result = bookRepository.findAll(spec)
                .stream()
                .map(BookResponseDto::from)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new BookNotFoundException("Book not found");
        }

        return result;
    }
}
