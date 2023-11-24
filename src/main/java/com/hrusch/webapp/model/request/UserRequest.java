package com.hrusch.webapp.model.request;

import com.hrusch.webapp.model.validation.ConfirmField;
import com.hrusch.webapp.model.validation.PasswordFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@ConfirmField(original = "password", confirmation = "repeatedPassword", message = "{equalPasswords.doNotMatch}")
@Getter
@Setter
public class UserRequest {

    @NotNull
    @Size(min = 3, max = 24, message = "{user.username.length}")
    private String username;

    @PasswordFormat
    @NotNull
    @Size(min = 8, message = "{user.password.length}")
    private String password;

    @PasswordFormat
    @NotNull
    @Size(min = 8, message = "{user.password.length}")
    private String repeatedPassword;
}
