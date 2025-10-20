package main.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateDto {

    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 12, message = "Username must be between 6 and 12 characters")
    private String username;

    @NotBlank(message = "Avatar is required")
    private String avatarUrl;
}
