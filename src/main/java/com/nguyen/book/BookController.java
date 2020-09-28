package com.nguyen.book;

import com.nguyen.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/books", headers = "X-API-VERSION=1.0")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookEntity> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("{id}")
    public BookEntity getBookById(@PathVariable String id) throws ApiException {
        return bookService.getBookById(Long.parseLong(id));
    }

    @PostMapping
    public BookEntity createBook(@RequestBody BookEntity bookEntity) throws ApiException {
        return bookService.createBook(bookEntity);
    }

    @PatchMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateBook(@RequestBody BookEntity bookEntity) throws ApiException {
        bookService.updateBook(bookEntity);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) throws ApiException {
        bookService.deleteBook(Long.parseLong(id));
    }
}
