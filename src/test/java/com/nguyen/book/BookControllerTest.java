package com.nguyen.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyen.audit.LoggerFactory;
import com.nguyen.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public LoggerFactory loggerFactory() {
            return new LoggerFactory();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private List<BookEntity> books;

    @BeforeEach
    public void setup() {
        books = List.of(
            BookEntity.builder()
                .id(1L)
                .title("Data Structures and Algorithms in Java")
                .isbn("0000111111")
                .author("Robert Lafore")
                .publishedDate(LocalDate.of(2020, 1, 30))
                .build(),
            BookEntity.builder()
                .id(2L)
                .title("Coding with Python: A Simple and Effective Guide to Coding With Python")
                .isbn("0000222222")
                .author("Alvaro Scrivano")
                .publishedDate(LocalDate.of(2020, 2, 28))
                .build()
        );
    }

    @Test
    public void verifyGetBooks() throws Exception {
        when(bookService.getBooks()).thenReturn(books);

        this.mockMvc.perform(
            get("/api/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("X-API-VERSION", "1.0")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id", equalTo(1)))
        .andExpect(jsonPath("$.[0].title", equalTo("Data Structures and Algorithms in Java")))
        .andExpect(jsonPath("$.[0].isbn", equalTo("0000111111")))
        .andExpect(jsonPath("$.[0].author", equalTo("Robert Lafore")))
        .andExpect(jsonPath("$.[1].id", equalTo(2)))
        .andExpect(jsonPath("$.[1].title", equalTo("Coding with Python: A Simple and Effective Guide to Coding With Python")))
        .andExpect(jsonPath("$.[1].isbn", equalTo("0000222222")))
        .andExpect(jsonPath("$.[1].author", equalTo("Alvaro Scrivano")));

        verify(bookService).getBooks();
    }

    @Test
    public void verifyGetBookById() throws Exception, ApiException {
        when(bookService.getBookById(1L)).thenReturn(books.get(0));
        this.mockMvc.perform(
            get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("X-API-VERSION", "1.0")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.title", equalTo("Data Structures and Algorithms in Java")))
        .andExpect(jsonPath("$.isbn", equalTo("0000111111")))
        .andExpect(jsonPath("$.author", equalTo("Robert Lafore")));

        verify(bookService).getBookById(1L);
    }

    @Test
    public void verifyGetBookById_NotFound() throws Exception, ApiException {
        when(bookService.getBookById(1L)).thenThrow(ApiException.builder().status(HttpStatus.NOT_FOUND).message("Not Found").build());

        this.mockMvc.perform(
            get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("X-API-VERSION", "1.0")
        ).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", equalTo("Not Found")));

        verify(bookService).getBookById(1L);
    }

    @Test
    public void verifyCreate() throws Exception, ApiException {
        String json = "{\"title\":\"Coding with Python: A Simple and Effective Guide to Coding With Python\",\"isbn\":\"0000222222\",\"author\":\"Alvaro Scrivano\",\"publishedDate\":\"2020-02-28\"}";
        BookEntity created = objectMapper.readValue(json, BookEntity.class);
        when(bookService.createBook(created)).thenReturn(books.get(1));
        this.mockMvc.perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("X-API-VERSION", "1.0")
                .content(json)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(2)))
        .andExpect(jsonPath("$.title", equalTo("Coding with Python: A Simple and Effective Guide to Coding With Python")))
        .andExpect(jsonPath("$.isbn", equalTo("0000222222")))
        .andExpect(jsonPath("$.author", equalTo("Alvaro Scrivano")));

        verify(bookService).createBook(created);
    }

    @Test
    public void verifyUpdate() throws Exception, ApiException {
        String json = "{\"id\":\"1\", \"title\":\"updated the title of this book\",\"isbn\":\"0000222222\",\"author\":\"Alvaro Scrivano\",\"publishedDate\":\"2020-02-28\"}";
        this.mockMvc.perform(
            patch("/api/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("X-API-VERSION", "1.0")
                .content(json)
        )
        .andExpect(status().isNoContent());

        BookEntity updated = objectMapper.readValue(json, BookEntity.class);
        verify(bookService).updateBook(updated);
    }

    @Test
    public void verifyDelete() throws Exception, ApiException {
        this.mockMvc.perform(
            delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("X-API-VERSION", "1.0")
        )
        .andExpect(status().isNoContent());

        verify(bookService).deleteBook(1L);
    }

    @Test
    public void verifyDelete_NotFound() throws Exception, ApiException {
        doThrow(ApiException.builder().status(HttpStatus.NOT_FOUND).build()).when(bookService).deleteBook(1L);

        this.mockMvc.perform(
            delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("X-API-VERSION", "1.0")
        )
        .andExpect(status().isNotFound());

        verify(bookService).deleteBook(1L);
    }
}
