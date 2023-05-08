package project2.services;

import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project2.models.Book;
import project2.models.Person;
import project2.repositories.PeopleRepository;
import java.util.Date;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){return peopleRepository.findAll();}

    public Person findOne(int id){
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public List<Book> getPersonBooks(int id){
        Optional<Person> person = peopleRepository.findById(id);
         if(person.isPresent()){
             Hibernate.initialize(person.get().getBooks());

             person.get().getBooks().forEach(book -> {
                 long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                 if (diffInMillies > 864000000) // 10 days
                     book.setExpired(true);
             });
             return person.get().getBooks();

         } else{
             return Collections.emptyList();
         }

    }

    public Optional<Person> getByFullName(String name){
        return peopleRepository.findByFullName(name);
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){peopleRepository.deleteById(id);}
}
