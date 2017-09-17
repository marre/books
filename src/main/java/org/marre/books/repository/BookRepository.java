package org.marre.books.repository;

import org.marre.books.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends MongoRepository<Book, UUID> {
    /**
     * Lookup books by their ISBN-10 id.
     * @param isbn ISBN-10
     * @return A list of books found. An empty list is returned if no books were found.
     */
    List<Book> findByIsbn(String isbn);
}
