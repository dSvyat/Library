package project2.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project2.models.Person;
import project2.services.PeopleService;

@Component
public class PersonValidator implements Validator {
    PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService){
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if (peopleService.getByFullName(person.getFullName()).isPresent())
            errors.rejectValue("fullName", "", "Person already exists");
    }
}
