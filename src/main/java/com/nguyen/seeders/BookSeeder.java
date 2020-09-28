package com.nguyen.seeders;

import com.nguyen.book.BookEntity;
import com.nguyen.book.BookRepository;
import com.nguyen.audit.LoggerFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class BookSeeder implements ApplicationRunner {
    private final Logger logger;

    private final BookRepository bookRepository;

    public BookSeeder(BookRepository bookRepository, LoggerFactory loggerFactory) {
        this.bookRepository = bookRepository;
        this.logger = loggerFactory.getLogger(BookSeeder.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Start seeding books for book library");

        List<BookEntity> seededBooks = List.of(
            BookEntity.builder()
                .title("Data Structures and Algorithms in Java")
                .isbn("0000111111")
                .author("Robert Lafore")
                .publishedDate(LocalDate.of(2018, 01, 30))
                .build(),
            BookEntity.builder()
                .title("Coding with Python: A Simple and Effective Guide to Coding With Python")
                .isbn("0000222222")
                .author("Alvaro Scrivano")
                .publishedDate(LocalDate.of(2019, 2, 28))
                .build(),
            BookEntity.builder()
                .title("Web Design with HTML, CSS, JavaScript and jQuery Set")
                .isbn("0000333333")
                .author("Jon Duckett")
                .publishedDate(LocalDate.of(2020, 3, 31))
                .build(),
            BookEntity.builder().title("Learn Java And Master Writing Code")
                .isbn("0000444444")
                .author("Sar Maroof")
                .publishedDate(LocalDate.of(2020, 4, 30))
                .build()
        );

        seededBooks.forEach(book -> {
            if (!bookRepository.findBookEntityByTitle(book.getTitle()).isPresent()) {
                bookRepository.save(book);
            }
        });

        logger.info("Seeding is successfully done.");
    }
}
