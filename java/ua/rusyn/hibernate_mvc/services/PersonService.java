package ua.rusyn.hibernate_mvc.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.rusyn.hibernate_mvc.model.Book;
import ua.rusyn.hibernate_mvc.model.Person;
import ua.rusyn.hibernate_mvc.repository.BookRepository;
import ua.rusyn.hibernate_mvc.repository.PersonRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;

    public PersonService(PersonRepository personRepository, BookRepository bookRepository) {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person find(int id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }
    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }
    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }


    public List<Book>getBooksByPersonId(int id) {
        Optional<Person> person = personRepository.findById(id);
        person.get().getBook().forEach(book -> {
            long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
            if (diffInMillies>864000000)
                book.setExpired(true);
        });
        return person.map(p -> p.getBook()).orElse(Collections.emptyList());
    }
}
