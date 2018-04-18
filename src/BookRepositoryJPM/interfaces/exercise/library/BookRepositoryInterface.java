package BookRepositoryJPM.interfaces.exercise.library;

import BookRepositoryJPM.src.exercise.library.Book;

public interface BookRepositoryInterface {
    Book retrieveBook(String isbn);
}
