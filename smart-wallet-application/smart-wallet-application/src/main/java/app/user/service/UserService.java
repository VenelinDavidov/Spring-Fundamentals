
package app.user.service;

import app.exception.DomainException;
import app.subscription.model.Subscription;
import app.subscription.service.SubscriptionService;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.wallet.model.Wallet;
import app.wallet.service.WalletService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionService subscriptionService;
    private final WalletService walletService;


    //Constructor
    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       SubscriptionService subscriptionService,
                       WalletService walletService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.subscriptionService = subscriptionService;
        this.walletService = walletService;
    }



    // Method for Login
    public User login(LoginRequest loginRequest) {

        Optional <User> optionalUser = userRepository.findByUsername (loginRequest.getUsername ());

        if (optionalUser.isEmpty ()) {

            throw new DomainException ("User with username=[%s] or password [%s] are incorrect."
                    .formatted (loginRequest.getUsername (), loginRequest.getPassword ()), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get ();

        if (!passwordEncoder.matches (loginRequest.getPassword (), user.getPassword ())) {
            throw new DomainException ("User with username=[%s] or password [%s] are incorrect."
                    .formatted (loginRequest.getUsername (), loginRequest.getPassword ()), HttpStatus.BAD_REQUEST);
        }

        return user;
    }




    //Method register
    @Transactional
    public User register(RegisterRequest registerRequest) {

        Optional <User> optionalUser = userRepository.findByUsername (registerRequest.getUsername ());

        if (optionalUser.isPresent ()) {
            throw new DomainException ("User with username=[%s] already exist."
                    .formatted (registerRequest.getUsername ()), HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.save (initializeNewUserAccount (registerRequest));


        subscriptionService.createDefaultSubscription (user);
        walletService.createNewWallet (user);

        log.info ("Successfully created new user for username [%s] with id [%s].".formatted (user.getUsername (), user.getId ()));

        return userRepository.save (user);
    }



    public void editUserDetails(UUID userId, UserEditRequest userEditRequest) {

        User user = getById(userId);

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setEmail(userEditRequest.getEmail());
        user.setProfilePicture(userEditRequest.getProfilePicture());

        userRepository.save(user);

    }



    //create method initialize
    private User initializeNewUserAccount(RegisterRequest dto) {

        return User.builder ()
                .username (dto.getUsername ())
                .password (passwordEncoder.encode (dto.getPassword ()))
                .role (UserRole.USER)
                .isActive (true)
                .country (dto.getCountry ())
                .createdOn (LocalDateTime.now ())
                .updatedOn (LocalDateTime.now ())
                .build ();
    }


    public List <User> getAllUsers() {
        return userRepository.findAll ();
    }



    public User getById(UUID uuid) {

        return userRepository.findById (uuid)
                .orElseThrow (()-> new DomainException ("User with id [%s] doesn't exist"
                .formatted (uuid),HttpStatus.BAD_REQUEST));
    }


    public void switchStatus(UUID userId) {

        User user = userRepository.getById (userId);
        user.setActive (!user.isActive ());
//        if (user.isActive ()){
//            user.setActive (false);
//        }else{
//            user.setActive (true);
//        }

        userRepository.save (user);
    }


    public void switchRole(UUID userId) {

        User user = userRepository.getById (userId);

        if (user.getRole () == UserRole.USER){
            user.setRole (UserRole.ADMIN);
        }else{
            user.setRole (UserRole.USER);
        }

        userRepository.save (user);
    }
}
