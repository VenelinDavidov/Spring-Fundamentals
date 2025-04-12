package main.init;

import main.model.Transaction;
import main.model.User;
import main.repository.TransactionRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public DatabaseInitializer(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.findAll().isEmpty()) {
            return;
        }

        User userOne = saveUser("https://avatars.githubusercontent.com/u/100943442?v=4",
                "Vik",
                "Mandarin",
                "Bulgaria",
                10.50,
                "(+359) 929292929",
                "Sofia, Borovo"
        );
        User userTwo = saveUser("https://avatars.githubusercontent.com/u/119004862?v=4",
                "Peter",
                "BigPapa",
                "France",
                789.00,
                "(+33) 140000000",
                "Paris, Belleville"
        );
        User userThree = saveUser("https://static.vecteezy.com/system/resources/previews/028/794/707/non_2x/cartoon-cute-school-boy-photo.jpg",
                "Kris",
                "OneManArmy",
                "United Kingdom",
                1500.00,
                "(+44) 7015212467",
                "London, Chiswick"
        );

        saveTransaction(userOne, "DEPOSIT", new BigDecimal("10.07"), LocalDate.of(2023, 12, 1));
        saveTransaction(userOne, "WITHDRAWAL", new BigDecimal("1.80"), LocalDate.of(2021, 5, 2));
        saveTransaction(userOne, "DEPOSIT", new BigDecimal("5.40"), LocalDate.of(2022, 2, 9));
        saveTransaction(userTwo, "WITHDRAWAL", new BigDecimal("6.80"), LocalDate.of(2021, 9, 13));
        saveTransaction(userTwo, "DEPOSIT", new BigDecimal("6.98"), LocalDate.of(2022, 8, 17));
        saveTransaction(userTwo, "WITHDRAWAL", new BigDecimal("11.17"), LocalDate.of(2022, 7, 6));
        saveTransaction(userTwo, "DEPOSIT", new BigDecimal("29.19"), LocalDate.of(2022, 2, 4));
        saveTransaction(userThree, "DEPOSIT", new BigDecimal("17.70"), LocalDate.of(2023, 2, 22));
        saveTransaction(userThree, "DEPOSIT", new BigDecimal("6.40"), LocalDate.of(2024, 1, 3));
    }

    private void saveTransaction(User owner, String type, BigDecimal amount, LocalDate createdOn) {

        Transaction transaction = Transaction.builder()
                .owner(owner)
                .type(type)
                .createdOn(createdOn)
                .amount(amount)
                .build();

        transactionRepository.save(transaction);
    }

    private User saveUser(
            String profilePictureUrl,
            String firstName,
            String username,
            String country,
            double balance,
            String phoneNumber,
            String address) {

        User user = User.builder()
                .profilePictureUrl(profilePictureUrl)
                .firstName(firstName)
                .username(username)
                .country(country)
                .balance(balance)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();

        return userRepository.save(user);
    }
}
