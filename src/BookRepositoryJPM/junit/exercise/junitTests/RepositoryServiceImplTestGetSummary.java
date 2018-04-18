package BookRepositoryJPM.junit.exercise.junitTests;

import BookRepositoryJPM.src.exercise.library.BookNotFoundException;
import BookRepositoryJPM.src.exercise.library.BookServiceImpl;
import BookRepositoryJPM.src.exercise.library.BookWrongISBNInputException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RepositoryServiceImplTestGetSummary {

    private BookServiceImpl bookService = new BookServiceImpl();

    @Test(expected = BookWrongISBNInputException.class)
    public void testGetBookSummaryEmptyIsbn() throws BookNotFoundException, BookWrongISBNInputException {
        bookService.getBookSummary("");
    }

    @Test(expected = BookWrongISBNInputException.class)
    public void testGetBookSummaryWrongIsbn() throws BookNotFoundException, BookWrongISBNInputException {
        bookService.getBookSummary("INVALID-TEXT");
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetBookSummaryMissingBook() throws BookNotFoundException, BookWrongISBNInputException {
        bookService.getBookSummary("ISBN-777");
    }

    @Test
    public void testGetBookSummaryRightIsbn() throws BookNotFoundException, BookWrongISBNInputException {
        String book001 = bookService.getBookSummary("ISBN-001");
        assertTrue(book001, book001.equals("[ISBN-001] Harry Potter and the Deathly Hallows - Sorcery and Magic."));
        assertFalse(bookService.getBookSummary("ISBN-001").equals("Harry Potter and the Black Riders"));

        String book002 = bookService.getBookSummary("ISBN-002");
        assertTrue(book002, book002.equals("[ISBN-002] The Player of Games - Jernau Morat Gurgeh. The Player of Games. Master of every..."));
        assertFalse(book002, book002
                .equals("[ISBN-002] The Player of Games - Jernau Morat Gurgeh. The Player of Games. Master of every board, computer and strategy."));

        String book003 = bookService.getBookSummary("ISBN-003");
        assertTrue(book003, book003
                .equals("[ISBN-003] Genius: Richard Feynman and Modern Physics - A brilliant interweaving of Richard Feynman's colourful life and a..."));
        assertFalse(book003, book003
                .equals("[ISBN-003] Genius: Richard Feynman and Modern Physics - A brilliant interweaving of Richard Feynman's colourful life and a detailed and accessible account of his theories and experiments."));
    }
}
