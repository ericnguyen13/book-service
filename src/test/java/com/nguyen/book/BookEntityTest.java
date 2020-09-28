package com.nguyen.book;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class BookEntityTest {
    @Test
    public void verifyBookEntityBuilder() {
        BookEntity book = BookEntity.builder().isbn("111").author("Eric Nguyen").build();
        BookEntity clone = book.toBuilder().build();
        assertEquals(book, clone);
    }

    @Test
    public void verifyBookEntityToString() {
        BookEntity book = BookEntity.builder().id(1L).isbn("0000").author("Eric Nguyen").publishedDate(LocalDate.of(2000, 1, 1)).build();
        assertEquals("BookEntity(id=1, title=null, isbn=0000, author=Eric Nguyen, publishedDate=2000-01-01)", book.toString());
        assertEquals("BookEntity.BookEntityBuilder(id=1, title=null, isbn=0000, author=Eric Nguyen, publishedDate=2000-01-01)", book.toBuilder().toString());
    }
}
