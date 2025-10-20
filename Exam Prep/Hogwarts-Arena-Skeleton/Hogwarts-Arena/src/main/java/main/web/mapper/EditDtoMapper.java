package main.web.mapper;

import lombok.experimental.UtilityClass;
import main.model.Wizard;
import main.web.dto.ProfileUpdateDto;

@UtilityClass
public class EditDtoMapper {

    // convert wizard to profile update dto
    public static ProfileUpdateDto mapToProfileUpdateDto(Wizard wizard) {

        return  ProfileUpdateDto.builder()
                .username(wizard.getUsername())
                .avatarUrl(wizard.getAvatarUrl())
                .build();
    }
}
