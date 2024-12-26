package com.mansitkr;

import java.util.Map;
import java.time.Year;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.mansitkr.exceptions.UserExistsException;
import com.mansitkr.exceptions.BookNotFoundException;
import com.mansitkr.exceptions.PermissionDeniedException;
import com.mansitkr.exceptions.BookAlreadyBorrowedException;


class LibraryTest {

    Library library = new Library("mansi");
    @Test
    public void testShouldFailWithoutProperConstructor() {
        assertNotNull(library);
    }

    @Test
    public void testLibraryNameShouldNotbeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Library(null));
    }

    @Test
    public void testLibraryNameShouldNotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Library(""));
    }

    @Test
    public void testLibraryNameShouldBeGreaterThan4Characters() {
        assertThrows(IllegalArgumentException.class, () -> new Library("mans"));
    }

    @Test
    public void testShouldThrowExceptionIfUserIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addUser(null));
        assertEquals("User should not be null", exception.getMessage());
    }

    @Test
    public void testShouldAllowOnlyPermittedUserToAddBook() {
        User user = new User("mansi", User.Role.LIBRARIAN);

        Book book = new Book("9780132350884", "Clean Code", "Robert Cecil Martin", Year.of(2012));
        library.addBook(user, book);

        Book storedBook = library.getBookByISBN("9780132350884");

        assertNotNull(storedBook);
        assertEquals(book, storedBook);
    }

    @Test
    public void testShouldThrowExceptionIfUnauthorizedUserAddBook() {
        User user = new User("dhruv", User.Role.USER);

        Book book = new Book("9780132350884", "Clean Code", "Robert Cecil Martin", Year.of(2012));
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () -> library.addBook(user, book));
        assertEquals("You are not authorized to add book", exception.getMessage());
    }

    @Test
    public void testShouldAddUserToLibrary() {
        User librarian = new User("mansi", User.Role.LIBRARIAN);

        library.addUser(librarian);

        User user = library.getUserByName("mansi");
        assertEquals(librarian, user);
    }

    @Test
    public void testShouldNotAllowDuplicateUsers() {
        User primaryLibrarian = new User("mansi", User.Role.LIBRARIAN);
        User secondaryLibrarian = new User("mansi", User.Role.LIBRARIAN);

        library.addUser(primaryLibrarian);
        UserExistsException exception = assertThrows(UserExistsException.class, () -> library.addUser(secondaryLibrarian));
        assertEquals("User already exists in catalog", exception.getMessage());
    }

    @Test
    public void testShouldFetchUserByUsername() {
        User primaryLibrarian = new User("mansi", User.Role.LIBRARIAN);

        library.addUser(primaryLibrarian);
        User fetchedUser = library.getUserByName("mansi");
        assertEquals(primaryLibrarian, fetchedUser);
    }

    @Test
    public void testShouldRetrieveAllAvailableBooks() {
        User librarian = new User("mansi", User.Role.LIBRARIAN);
        Book book1 = new Book("9780132350884", "Clean Code", "Robert Cecil Martin", Year.of(2012));
        Book book2 = new Book("9780134685991", "Effective Java", "Joshua Bloch", Year.of(2018));

        library.addUser(librarian);
        library.addBook(librarian, book1);
        library.addBook(librarian, book2);

        Map<String, Book> availableBooks = library.viewAvailableBooks();

        assertEquals(2, availableBooks.size());
        assertTrue(availableBooks.containsKey("9780132350884"));
        assertTrue(availableBooks.containsKey("9780134685991"));
    }

    @Test
    public void testShouldReturnUnmodifiableHashMap() {
        User librarian = new User("mansi", User.Role.LIBRARIAN);
        Book book1 = new Book("9780132350884", "Clean Code", "Robert Cecil Martin", Year.of(2012));

        library.addUser(librarian);
        library.addBook(librarian, book1);

        Map<String, Book> availableBooks = library.viewAvailableBooks();

        assertThrows(UnsupportedOperationException.class, () -> availableBooks.put("9780134685991", new Book("9780134685991", "Effective Java", "Joshua Bloch", Year.of(2018))));
    }

    @Test
    public void testShouldAllowToBorrowBookFromLibrary() {
        User librarian = new User("mansi", User.Role.LIBRARIAN);
        User user = new User("dhruv", User.Role.USER);
        Book book = new Book("9780132350884", "Clean Code", "Robert Cecil Martin", Year.of(2012));

        library.addUser(librarian);
        library.addUser(user);
        library.addBook(librarian, book);

        library.borrowBook(user, "9780132350884");

        Book borrowedBook = library.getBookByISBN("9780132350884");
        assertNull(borrowedBook, "borrowedBook should be null as it has been borrowed earlier.");
    }

    @Test
    public void testShouldThrowExceptionWhenBookNotFoundDuringBorrowRequest() {

        User user = new User("mansi", User.Role.USER);

        library.addUser(user);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> library.borrowBook(user, "9780132350884"));
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    public void testShouldThrowExceptionWhenBookIsAlreadyBorrowed() {

        User librarian = new User("mansi", User.Role.LIBRARIAN);
        User user1 = new User("dhruv", User.Role.USER);
        User user2 = new User("rishva", User.Role.USER);
        Book book = new Book("9780132350884", "Clean Code", "Robert Cecil Martin", Year.of(2012));

        library.addUser(librarian);
        library.addUser(user1);
        library.addUser(user2);
        library.addBook(librarian, book);

        library.borrowBook(user1, "9780132350884");

        BookAlreadyBorrowedException exception = assertThrows(BookAlreadyBorrowedException.class, () -> library.borrowBook(user2, "9780132350884"));
        assertEquals("Book is already borrowed", exception.getMessage());
    }

    @Test
    public void testShouldReturnBorrowerNameWhoBorrowedBook() {
        User librarian = new User("mansi", User.Role.LIBRARIAN);
        User user = new User("dhruv", User.Role.USER);
        Book book = new Book("9780132350884", "Clean Code", "Robert Cecil Martin", Year.of(2012));

        library.addUser(librarian);
        library.addUser(user);
        library.addBook(librarian, book);

        library.borrowBook(user, "9780132350884");

        String borrowerName = library.getBorrowerNameByISBN("9780132350884");

        assertEquals(user.getUserName(), borrowerName);
    }

    @Test
    public void testShouldAllowUserToReturnBookToLibrary() {
        User librarian = new User("mansi", User.Role.LIBRARIAN);
        User user = new User("dhruv", User.Role.USER);
        Book book = new Book("9780132350884", "Clean Code", "Robert Cecil Martin", Year.of(2012));

        library.addUser(librarian);
        library.addUser(user);
        library.addBook(librarian, book);

        library.borrowBook(user, "9780132350884");
        library.returnBook(user, "9780132350884");

        Book returnedBook = library.getBookByISBN("9780132350884");
        assertNotNull(returnedBook, "Returned book have be available in the books catalog.");
    }

    @Test
    public void testShouldThrowExceptionWhenUserReturnsBookThatIsNotBorrowedByHim() {
        User librarian = new User("mansi", User.Role.LIBRARIAN);
        User user1 = new User("dhruv", User.Role.USER);
        User user2 = new User("rishva", User.Role.USER);
        Book book = new Book("9780132350884", "Clean Code", "Robert Cecil Martin", Year.of(2012));

        library.addUser(librarian);
        library.addUser(user1);
        library.addUser(user2);
        library.addBook(librarian, book);

        library.borrowBook(user1, "9780132350884");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.returnBook(user2, "9780132350884"));
        assertEquals("book was not borrowed by this user", exception.getMessage());
    }

    @Test
    public void testShouldThrowExceptionWhenNoOneBorrowedBook() {
        User librarian = new User("mansi", User.Role.LIBRARIAN);
        User user1 = new User("dhruv", User.Role.USER);
        Book book = new Book("9780132350884", "Clean Code", "Robert Cecil Martin", Year.of(2012));

        library.addUser(librarian);
        library.addUser(user1);
        library.addBook(librarian, book);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> library.returnBook(user1, "9780132350884"));
        assertEquals("Book was not borrowed by any user", exception.getMessage());
    }


}