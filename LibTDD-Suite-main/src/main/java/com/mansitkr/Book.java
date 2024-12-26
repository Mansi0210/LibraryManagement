package com.mansitkr;

import java.time.Year;

import static com.mansitkr.utils.StringValidator.validateString;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private Year publicationYear;

    public Book(String isbn, String title, String author, Year publicationYear) {

        validateRequiredAttributes(isbn, title, author, publicationYear);

        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    private void validateRequiredAttributes(String isbn, String title, String author, Year publicationYear) {
        validateString(isbn, "ISBN should not be null or empty");
        validateString(title, "title should not be null or empty");
        validateString(author, "author should not be null or empty");
        if(publicationYear == null){
            throw new IllegalArgumentException("publication year should not be null");
        }
    }

    public String getISBN() {
        return isbn;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(object == null || (this.getClass() != object.getClass())) return false;
        Book book = (Book) object;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }
}
