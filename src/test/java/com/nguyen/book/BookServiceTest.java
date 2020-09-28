package com.nguyen.book;

import com.nguyen.audit.LoggerFactory;
import com.nguyen.exception.ApiException;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository repository;

    @Mock
    private Logger logger;

    @Mock
    private LoggerFactory loggerFactory;

    private BookService subject;

    @BeforeEach
    public void setup() {
        when(loggerFactory.getLogger(any())).thenReturn(logger);
        subject = new BookService(repository, loggerFactory);
    }

    @AfterEach
    public void verifyAll() {
        verify(loggerFactory).getLogger(BookService.class);
    }

    @Test
    public void verifyGetBooks() {
        List<BookEntity> expectedBooks = List.of(
            BookEntity.builder()
                .id(1L)
                .title("Book 1")
                .isbn("0000")
                .publishedDate(LocalDate.of(2018, 1, 30))
                .build(),
            BookEntity.builder()
                .id(2L)
                .title("Book 2")
                .isbn("1111")
                .publishedDate(LocalDate.of(2019, 5, 15))
                .build()
        );

        when(repository.findAll()).thenReturn(expectedBooks);
        List<BookEntity> actualBooks = subject.getBooks();
        assertEquals(actualBooks, expectedBooks);
        verify(repository).findAll();
    }

    @Test
    public void verifyGetBookByIdSuccess() throws ApiException {
        long id = 1L;
        BookEntity book =
            BookEntity.builder()
                .id(id)
                .title("Book 1")
                .isbn("0000")
                .publishedDate(LocalDate.of(2018, 01, 30))
                .build();
        when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(book));
        BookEntity foundBook = subject.getBookById(id);
        assertEquals(foundBook, book);
        verify(repository).findById(id);
    }

    @Test
    public void verifyGetBookById_Throws_ApiException_With_404() {
        long id = 1L;
        when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> {
            subject.getBookById(id);
            verify(repository).findById(id);
        });
    }

    @Test
    public void verifyCreateBookSuccess() throws ApiException {
        BookEntity book =
            BookEntity.builder()
                .title("Book 1")
                .isbn("0000")
                .publishedDate(LocalDate.of(2018, 01, 30))
                .build();
        when(repository.save(book)).thenReturn(book);
        BookEntity createdBook = subject.createBook(book);
        assertEquals(book, createdBook);
        verify(repository).save(book);
    }

    @Test
    public void verifyCreateBook_ThrowsException() {
        BookEntity book = BookEntity.builder().id(1L).build();
        when(repository.save(book)).thenThrow(new IllegalArgumentException("Failed to create book entity"));
        assertThrows(ApiException.class, () -> {
            subject.createBook(book);
            verify(repository).save(book);
        });
    }

    @Test
    public void verifyUpdateBookSuccess() throws ApiException {
        BookEntity book =
            BookEntity.builder()
                .id(1L)
                .title("Book 1")
                .isbn("0000")
                .publishedDate(LocalDate.of(2018, 01, 30))
                .build();
        when(repository.save(book)).thenReturn(book);
        subject.updateBook(book);
        verify(repository).save(book);
    }

    @Test
    public void verifyUpdateBook_ThrowsException() {
        BookEntity book = BookEntity.builder().build();
        when(repository.save(any())).thenThrow(new IllegalArgumentException("Failed to update book entity"));
        assertThrows(ApiException.class, () -> {
           subject.updateBook(book);
           verify(repository).save(book);
        });
    }

    @Test
    public void verifyDeleteBookSuccess() throws ApiException {
        long id = 1L;
        BookEntity book = BookEntity.builder().build();
        when(repository.findById(id)).thenReturn(Optional.of(book));
        subject.deleteBook(id);
        verify(repository).findById(id);
        verify(repository).delete(book);
    }

    @Test
    public void verifyDeleteBook_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> {
            subject.deleteBook(1L);
            verify(repository).findById(1L);
        });
    }
}
