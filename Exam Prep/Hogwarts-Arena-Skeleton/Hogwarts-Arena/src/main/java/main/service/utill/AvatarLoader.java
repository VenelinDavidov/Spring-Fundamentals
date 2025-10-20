package main.service.utill;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class AvatarLoader {

    private List<String> avatarUrls;
    private final Random random = new Random();


    @PostConstruct
    public void loadAvatars() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(

                Objects.requireNonNull (getClass ().getResourceAsStream ("/avatars.txt"))))) {
            avatarUrls = reader.lines()
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalStateException("Could not load avatars.txt", e);
        }
    }

    public String getRandomAvatar() {
        if (avatarUrls == null || avatarUrls.isEmpty()) {
            throw new IllegalStateException("Avatar list is empty or not loaded");
        }
        return avatarUrls.get(random.nextInt(avatarUrls.size()));
    }
}
