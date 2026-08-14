package com.example.book_search;

public record BookResponseDto(
        Long id,
        String title,
        String author,
        String publisher,
        Integer price,
        Integer publishedYear

) {
    public static BookResponseDto from(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPrice(),
                book.getPublishedYear()
        );
    }
}
