package Philately.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank (message = "Username cannot be empty.")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 character!")
    private String username;

    @NotBlank (message = "Email cannot be empty.")
    @Email (message = "Not a valid email format.")
    private String email;

    @NotBlank (message = "Password cannot be empty.")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 character!")
    private String password;

    @NotBlank (message = "Password cannot be empty.")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 character!")
    private String confirmPassword;
}

