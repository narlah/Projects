package BookRepositoryJPM.interfaces.exercise.library;

import BookRepositoryJPM.src.exercise.library.Book;
import BookRepositoryJPM.src.exercise.library.BookNotFoundException;
import BookRepositoryJPM.src.exercise.library.BookWrongISBNInputException;

public interface BookServiceInterface {
    Book retrieveBook(String isbn) throws BookNotFoundException, BookWrongISBNInputException;

    String getBookSummary(String isbn) throws BookNotFoundException, BookWrongISBNInputException;
}
