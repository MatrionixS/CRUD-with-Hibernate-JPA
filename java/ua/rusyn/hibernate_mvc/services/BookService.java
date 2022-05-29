package ua.rusyn.hibernate_mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.rusyn.hibernate_mvc.model.Book;
import ua.rusyn.hibernate_mvc.model.Person;
import ua.rusyn.hibernate_mvc.repository.BookRepository;
import ua.rusyn.hibernate_mvc.repository.PersonRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;
    @Autowired
    public BookService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if(sortByYear)
            return bookRepository.findAll(Sort.by("year"));
        else
        return bookRepository.findAll();
    }

    public Book find(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }
    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }
    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void updateById(int id) {
        Optional<Book> byId = bookRepository.findById(id);
        byId.get().setOwner(null);
    }
    @Transactional
    public void assign(int id, Person selectedPerson) {
        bookRepository.findById(id).ifPresent(book -> {
                book.setOwner(selectedPerson);
                book.setTakenAt(new Date());});
    }

    public Person findByOwner(int id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.get().getOwner();
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if(sortByYear)
            return bookRepository.findAll(PageRequest.of(page,booksPerPage,Sort.by("year"))).getContent();
        else
            return bookRepository.findAll(PageRequest.of(page,booksPerPage)).getContent();
    }

    public List<Book> searchByTitle(String query) {
        return bookRepository.findByTitleStartingWith(query);
    }
}
