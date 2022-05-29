package ua.rusyn.hibernate_mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.rusyn.hibernate_mvc.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

}
