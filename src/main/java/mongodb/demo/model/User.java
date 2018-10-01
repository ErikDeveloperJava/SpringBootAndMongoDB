package mongodb.demo.model;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private int age;

    private Date birthDate;
}