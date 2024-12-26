package com.mansitkr;

import java.time.Year;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    public void testShouldThrowExceptionWhenISBNisNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Book(null, "Clean Code", "Robert Cecil Martin", Year.of(2012)));
        assertEquals("ISBN should not be null or empty", exception.getMessage());
    }

    @Test
    public void testShouldThrowExceptionWhenTitleIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Book("9780132350884", null, "Robert Cecil Martin", Year.of(2012)));
        assertEquals("title should not be null or empty", exception.getMessage());
    }

    @Test
    public void testShouldThrowExceptionWhenAuthorIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Book("9780132350884", "Clean Code", "", Year.of(2012)));
        assertEquals("author should not be null or empty", exception.getMessage());
    }

    @Test
    public void testShouldThrowExceptionWhenPublicationYearIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Book("9780132350884", "Clean Code", "Robert Cecil Martin", null));
        assertEquals("publication year should not be null", exception.getMessage());
    }
}