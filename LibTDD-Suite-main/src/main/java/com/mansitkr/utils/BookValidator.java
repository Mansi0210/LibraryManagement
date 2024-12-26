package com.mansitkr.utils;

import com.mansitkr.Book;
import com.mansitkr.exceptions.BookNotFoundException;

public class BookValidator {
    public static void validateBookNotNull(Book book, String message) {
        if (book == null) {
            throw new BookNotFoundException(message);
        }
    }
}
