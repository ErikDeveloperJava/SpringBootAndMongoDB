package mongodb.demo.config.security;


import lombok.Getter;
import mongodb.demo.model.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User{

    @Getter
    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList("AUTHENTICATED"));
        this.user = user;
    }
}
