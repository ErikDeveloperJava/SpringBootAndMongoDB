package mongodb.demo.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForm {

    @Length(min = 2,max = 13,message = "name length must be greater than 2 and less than 13")
    private String name;

    @Length(min = 2,max = 20,message = "surname length must be greater than 2 and less than 20")
    private String surname;

    @Length(min = 2,max = 40,message = "email length must be greater than 2 and less than 40")
    private String email;

    @Length(min = 2,max = 12,message = "password length must be greater than 2 and less than 12")
    private String password;

    @Length(min = 9,message = "invalid date format")
    private String birthDate;
}