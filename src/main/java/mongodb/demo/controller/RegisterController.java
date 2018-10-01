package mongodb.demo.controller;

import mongodb.demo.form.UserForm;
import mongodb.demo.model.User;
import mongodb.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@Valid UserForm userForm, BindingResult result){
        Date birthDate;
        if(result.hasErrors()){
            return "login";
        }else if(userService.getByEmail(userForm.getEmail()).isPresent()){
            result.addError(new FieldError("userForm","email","user with email '" + userForm.getEmail() + "' already exists"));
            return "login";
        }else if((birthDate = parseBirthDate(userForm.getBirthDate())) == null){
            result.addError(new FieldError("userForm","birthDate","invalid birth date"));
            return "login";
        }else {
            int age = parseAge(userForm.getBirthDate());
            User user = User
                    .builder()
                    .name(userForm.getName())
                    .surname(userForm.getSurname())
                    .email(userForm.getEmail())
                    .password(userForm.getPassword())
                    .age(age)
                    .birthDate(birthDate)
                    .build();
            userService.add(user);
            return "redirect:/";
        }
    }

    private Date parseBirthDate(String strDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            int year = Integer.parseInt(strDate.split("/")[2]);
            if(year < 1930 || year > 2008){
                throw new IllegalAccessException();
            }
            return dateFormat.parse(strDate);
        } catch (Exception e) {
            return null;
        }
    }

    private int parseAge(String strDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String[] birthDateArray = strDate.split("/");
        int day = Integer.parseInt(birthDateArray[0].startsWith("0") ? birthDateArray[0].substring(1) : birthDateArray[0]);
        int month = Integer.parseInt(birthDateArray[1].startsWith("0") ? birthDateArray[1].substring(1) : birthDateArray[1]);
        int year = Integer.parseInt(birthDateArray[2]);
        String[] currentDateArray = dateFormat.format(new Date()).split("/");
        int currentDay = Integer.parseInt(currentDateArray[0].startsWith("0") ? currentDateArray[0].substring(1) : currentDateArray[0]);
        int currentMonth = Integer.parseInt(currentDateArray[1].startsWith("0") ? currentDateArray[1].substring(1) : currentDateArray[1]);
        int currentYear = Integer.parseInt(currentDateArray[2]);
        int size = 0;
        if(month == currentMonth){
            if(day > currentDay){
                size = 1;
            }
        }else if(month > currentMonth){
            size = 1;
        }
        return (currentYear - year) - size;
    }
}