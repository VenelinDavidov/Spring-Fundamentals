package main.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import main.model.House;
import main.model.WizardAlignment;
import org.hibernate.validator.constraints.URL;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 6, max = 12, message = "Username must be between 6 and 12 characters")
    private String username;

    @NotBlank
    @Min (value = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank
    @URL
    private String avatarUrl;

    @NotNull
    private House house;

    @NotNull
    private WizardAlignment alignment;

}
