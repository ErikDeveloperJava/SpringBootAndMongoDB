package mongodb.demo.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookForm {

    private String id;

    @Length(min = 2,max = 40,message = "title length must be greater than 1 and less than 41")
    private String title;

    @Length(min = 10,message = "title length must be greater than 10")
    private String description;

    @Range(min = 10,message = "price must be less than 9")
    private int price;
}