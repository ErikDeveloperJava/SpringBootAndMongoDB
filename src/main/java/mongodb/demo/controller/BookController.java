package mongodb.demo.controller;

import mongodb.demo.config.security.CurrentUser;
import mongodb.demo.form.BookForm;
import mongodb.demo.model.Book;
import mongodb.demo.service.BookService;
import mongodb.demo.util.pagination.MongoPage;
import mongodb.demo.util.pagination.MongoPaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/book")
public class BookController {

    @Value("${pagination.size}")
    private int size;

    @Autowired
    private BookService bookService;

    @Autowired
    private MongoPaginationUtil paginationUtil;

    @PostMapping("/add")
    public String add(@Valid BookForm form, BindingResult result,
                      Model model, @AuthenticationPrincipal CurrentUser currentUser){
        if(result.hasErrors()){
            int length = paginationUtil.calculateLength(bookService.count(),size);
            MongoPage page = paginationUtil.getPage("0",size,length);
            model.addAttribute("books",bookService.getAll(page));
            model.addAttribute("paginationLength",length);
            model.addAttribute("currentPage",page.getSkip() == 0 ? 0 : page.getSkip()/size);
            model.addAttribute("user",currentUser.getUser());
            model.addAttribute("action","/book/add");
            return "index";
        }else {
            Book book = Book
                    .builder()
                    .title(form.getTitle())
                    .description(form.getDescription())
                    .price(form.getPrice())
                    .createdDate(new Date())
                    .user(currentUser.getUser())
                    .build();
            bookService.add(book);
            return "redirect:/";
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id")String id){
        bookService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/{id}/update")
    public String updateGet(@PathVariable("id")String id,Model model,
                            @AuthenticationPrincipal CurrentUser currentUser){
        Optional<Book> optionalBook = bookService.getById(id);
        if(!optionalBook.isPresent() || !optionalBook.get().getUser().getId().equals(currentUser.getUser().getId())){
            return "redirect:/";
        }
        int length = paginationUtil.calculateLength(bookService.count(),size);
        MongoPage page = paginationUtil.getPage("0",size,length);
        model.addAttribute("books",bookService.getAll(page));
        model.addAttribute("paginationLength",length);
        model.addAttribute("currentPage",page.getSkip() == 0 ? 0 : page.getSkip()/size);
        model.addAttribute("user",currentUser.getUser());
        model.addAttribute("bookForm",getBookForm(optionalBook.get()));
        model.addAttribute("action","/book/update");
        return "index";
    }

    private BookForm getBookForm(Book book){
        return BookForm
                .builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .price(book.getPrice())
                .build();
    }

    @PostMapping("/update")
    public String update(@Valid BookForm bookForm,BindingResult result,Model model,
                         @AuthenticationPrincipal CurrentUser currentUser){
        if(result.hasErrors()){
            int length = paginationUtil.calculateLength(bookService.count(),size);
            MongoPage page = paginationUtil.getPage("0",size,length);
            model.addAttribute("books",bookService.getAll(page));
            model.addAttribute("paginationLength",length);
            model.addAttribute("currentPage",page.getSkip() == 0 ? 0 : page.getSkip()/size);
            model.addAttribute("user",currentUser.getUser());
            model.addAttribute("action","/book/update");
            return "index";
        }else {
            Optional<Book> optionalBook = bookService.getById(bookForm.getId());
            if(optionalBook.isPresent()){
                Book book = Book
                        .builder()
                        .id(bookForm.getId())
                        .title(bookForm.getTitle())
                        .description(bookForm.getDescription())
                        .createdDate(optionalBook.get().getCreatedDate())
                        .price(bookForm.getPrice())
                        .user(currentUser.getUser())
                        .build();
                bookService.update(book);
            }
            return "redirect:/";
        }

    }
}