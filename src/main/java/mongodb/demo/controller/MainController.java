package mongodb.demo.controller;

import mongodb.demo.config.security.CurrentUser;
import mongodb.demo.form.BookForm;
import mongodb.demo.form.UserForm;
import mongodb.demo.service.BookService;
import mongodb.demo.util.pagination.MongoPage;
import mongodb.demo.util.pagination.MongoPaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Value("${pagination.size}")
    private int size;

    @Autowired
    private BookService bookService;

    @Autowired
    private MongoPaginationUtil paginationUtil;

    @GetMapping("")
    public String main(@RequestParam(value = "page",required = false,defaultValue = "0")String strPage,
            Model model, @AuthenticationPrincipal CurrentUser currentUser){
        if(currentUser == null){
            model.addAttribute("userForm",new UserForm());
            return "login";
        }else {
            int length = paginationUtil.calculateLength(bookService.count(),size);
            MongoPage page = paginationUtil.getPage(strPage,size,length);
            model.addAttribute("books",bookService.getAll(page));
            model.addAttribute("paginationLength",length);
            model.addAttribute("bookForm",new BookForm());
            model.addAttribute("currentPage",page.getSkip() == 0 ? 0 : page.getSkip()/size);
            model.addAttribute("user",currentUser.getUser());
            model.addAttribute("action","/book/add");
            return "index";
        }
    }
}