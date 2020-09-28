package com.nguyen.book;

import com.nguyen.exception.ApiException;
import com.nguyen.audit.LoggerFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final Logger logger;

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository, LoggerFactory loggerFactory) {
        this.bookRepository = bookRepository;
        this.logger = loggerFactory.getLogger(BookService.class);
    }

    public List<BookEntity> getBooks() {
        return bookRepository.findAll();
    }

    public BookEntity getBookById(long id) throws ApiException {
        Optional<BookEntity> optional = bookRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }

        throw ApiException.builder().status(HttpStatus.NOT_FOUND).message("Not Found").build();
    }

    public BookEntity createBook(BookEntity bookEntity) throws ApiException {
        return this.save(bookEntity);
    }

    public void updateBook(BookEntity bookEntity) throws ApiException {
        this.save(bookEntity);
    }

    public void deleteBook(long id) throws ApiException {
        Optional<BookEntity> found = bookRepository.findById(id);
        if (found.isPresent()) {
            bookRepository.delete(found.get());
        } else {
            throw ApiException.builder().status(HttpStatus.NOT_FOUND).message("Not Found").build();
        }
    }

    protected BookEntity save(BookEntity bookEntity) throws ApiException {
        try {
            return bookRepository.save(bookEntity);
        } catch (Exception ex) {
            logger.error(ex);
            throw ApiException.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message(ex.getMessage()).build();
        }
    }
}
