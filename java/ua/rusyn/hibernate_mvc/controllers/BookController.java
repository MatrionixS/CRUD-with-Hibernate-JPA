package ua.rusyn.hibernate_mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.rusyn.hibernate_mvc.model.Book;
import ua.rusyn.hibernate_mvc.model.Person;
import ua.rusyn.hibernate_mvc.services.BookService;
import ua.rusyn.hibernate_mvc.services.PersonService;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService service;
    private final PersonService personService;

    @Autowired
    public BookController(BookService service, PersonService personService) {
        this.service = service;

        this.personService = personService;
    }

    @GetMapping
    public String homePage(Model model,@RequestParam(value = "page",required = false)Integer page,
                           @RequestParam(value = "books_per_page",required = false)Integer booksPerPage,
                           @RequestParam(value = "sort_by_year",required = false)boolean sortByYear){
        if(page == null || booksPerPage == null)
            model.addAttribute("books", service.findAll(sortByYear));
        else
            model.addAttribute("books",service.findWithPagination(page,booksPerPage,sortByYear));

        return "book/home";
    }
    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id")int id,@ModelAttribute("person")Person person, Model model){
        model.addAttribute("book",service.find(id));
        model.addAttribute("owner",service.findByOwner(id));
        model.addAttribute("people",personService.findAll());
        return "book/book";
    }
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id")int id,Model model){
        model.addAttribute("book",service.find(id));
        return "book/edit";
    }
    @PatchMapping("/{id}/edit")
    public String edit(@PathVariable("id")int id, @ModelAttribute("person") Book book){
        service.update(id,book);
        return "redirect:/book";
    }
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id")int id){
        service.delete(id);
        return "redirect:/book";
    }
    @GetMapping("/create")
    public String createForm(@ModelAttribute("book") Book book){
        return "book/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("book") Book book){
        service.save(book);
        return "redirect:/book";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id")int id){
        service.updateById(id);
        return "redirect:/book/"+id;
    }
    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id")int id,@ModelAttribute("person") Person selectedPerson){
        service.assign(id,selectedPerson);
        return "redirect:/book/"+id;
    }
    @GetMapping("/search")
    public String searchForm(){
        return "book/search";
    }
    @PostMapping("/search")
    public String search(Model model,@RequestParam("query")String query){
        model.addAttribute("books",service.searchByTitle(query));
        return "/book/search";
    }
}
