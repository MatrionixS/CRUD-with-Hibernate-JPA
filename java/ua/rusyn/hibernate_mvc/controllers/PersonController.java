package ua.rusyn.hibernate_mvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.rusyn.hibernate_mvc.model.Book;
import ua.rusyn.hibernate_mvc.model.Person;
import ua.rusyn.hibernate_mvc.services.PersonService;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService service) {
        this.personService = service;
    }

    @GetMapping
    public String homePage(Model model){
        model.addAttribute("people", personService.findAll());
        return "home";
    }
    @GetMapping("/{id}")
    @Transactional
    public String personPage(@PathVariable("id")int id,Model model){
        model.addAttribute("person", personService.find(id));
        List<Book> booksByPersonId = personService.getBooksByPersonId(id);
        if(!booksByPersonId.isEmpty())
        model.addAttribute("books", personService.getBooksByPersonId(id));
        else
            model.addAttribute("book", personService.getBooksByPersonId(id));
        return "person";
    }
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id")int id,Model model){
        model.addAttribute("person", personService.find(id));
        return "edit";
    }
    @PatchMapping ("/{id}/edit")
    public String edit(@PathVariable("id")int id, @ModelAttribute("person") Person person){
        personService.update(id,person);
        return "redirect:/people";
    }
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id")int id){
        personService.delete(id);
        return "redirect:/people";
    }
    @GetMapping("/create")
    public String createForm(@ModelAttribute("person") Person person){
        return "create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("person") Person person){
        personService.save(person);
        return "redirect:/people";
    }
}
