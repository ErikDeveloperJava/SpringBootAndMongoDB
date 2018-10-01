package mongodb.demo.model;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    private String id;

    private String title;

    private String description;

    private int price;

    private Date createdDate;

    private User user;
}