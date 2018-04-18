package BookRepositoryJPM.src.exercise.library;

import BookRepositoryJPM.interfaces.exercise.library.BookServiceInterface;

import java.util.StringTokenizer;

public class BookServiceImpl implements BookServiceInterface {
    private static final String ISBN_PREFIX = "ISBN-";
    private BookRepositoryImpl bookRepository = new BookRepositoryImpl();

    public Book retrieveBook(String isbn) throws BookNotFoundException, BookWrongISBNInputException {
        testForWrongISBNPrefix(isbn);
        return getBookFromRepo(isbn);

    }

    public String getBookSummary(String isbn) throws BookNotFoundException, BookWrongISBNInputException {
        testForWrongISBNPrefix(isbn);
        Book book = getBookFromRepo(isbn);
        return "[" + isbn + "] " + book.getTitle() + " - " + getFirst10WordsAndMaybeAddDots(book.getDescription());
    }

    private void testForWrongISBNPrefix(String isbn) throws BookWrongISBNInputException {
        if (isbn.isEmpty() || !isbn.substring(0, 5).equals(ISBN_PREFIX))
            throw new BookWrongISBNInputException("The input for ISBN should have the following format 'ISBN-<number>.");
    }

    private Book getBookFromRepo(String isbn) throws BookNotFoundException {
        Book book = bookRepository.retrieveBook(isbn);
        if (book == null)
            throw new BookNotFoundException();
        return book;
    }

    private String getFirst10WordsAndMaybeAddDots(String arg) {
        StringTokenizer st = new StringTokenizer(arg);
        StringBuilder stringBuilder = new StringBuilder(10);
        int i = 0;
        for (; i < 10 && st.hasMoreTokens(); i++) {
            stringBuilder.append(st.nextToken());
            stringBuilder.append(" ");
        }
        if (i <= 9)
            return stringBuilder.toString().trim();
        else
            return stringBuilder.toString().trim() + "...";
    }
}
