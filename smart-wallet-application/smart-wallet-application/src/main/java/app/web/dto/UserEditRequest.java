package app.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEditRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String profilePicture;
}
