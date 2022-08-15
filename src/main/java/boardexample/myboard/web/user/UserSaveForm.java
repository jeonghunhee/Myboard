package boardexample.myboard.web.user;

import boardexample.myboard.domain.user.User;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class UserSaveForm {

    @Email @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    public UserSaveForm(String email, String password, String phone) {
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .phone(phone)
                .build();
    }


}
