package com.hrusch.webapp.io.request;

import com.hrusch.webapp.io.request.validation.PasswordFormat;
import com.hrusch.webapp.io.request.validation.ConfirmField;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@ConfirmField(original = "password", confirmation = "repeatedPassword", message = "{equalPasswords.doNotMatch}")
@Getter
public class UserRequest {

    @NotNull
    @Size(min = 2, max = 24, message = "{user.username.length}")
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
