package main.service;

import main.model.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {

        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id %s not found.".formatted(id)));
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }
}
