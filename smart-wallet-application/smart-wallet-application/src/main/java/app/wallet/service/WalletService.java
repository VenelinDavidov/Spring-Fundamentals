package app.wallet.service;

import app.exception.DomainException;
import app.transaction.model.Transaction;
import app.transaction.model.TransactionStatus;
import app.transaction.model.TransactionType;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.wallet.model.Wallet;
import app.wallet.model.WalletStatus;
import app.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Slf4j
@Service
public class WalletService {

    private static final String SMART_WALLET_LTD = "Smart Wallet LTD";

    private final WalletRepository walletRepository;
    private final TransactionService transactionService;


    //Constructor
    @Autowired
    public WalletService(WalletRepository walletRepository,
                         TransactionService transactionService) {
        this.walletRepository = walletRepository;
        this.transactionService = transactionService;
    }


    public void createNewWallet(User user) {

        Wallet wallet = walletRepository.save (initializeNewWallet (user));

        log.info ("Successfully created new wallet with id [%s] and balance [%.2f]."
                .formatted (wallet.getId (), wallet.getBalance ()));

    }




    //Method change
    @Transactional
    public Transaction topUp(UUID walletId, BigDecimal amount) {

        Wallet wallet = getWalletById (walletId);


        if (wallet.getStatus () == WalletStatus.INACTIVE) {

            return transactionService.createNewTransaction (
                    wallet.getOwner (),
                    SMART_WALLET_LTD,
                    walletId.toString (),
                    amount,
                    wallet.getBalance (),
                    wallet.getCurrency (),
                    TransactionType.DEPOSIT,
                    TransactionStatus.FAILED,
                    "Top Up %.2f".formatted (amount.doubleValue ()),
                    "Inactive wallet");
        }

        wallet.setBalance (wallet.getBalance ().add (amount));
        //  wallet.setCreatedOn (LocalDateTime.now());
        wallet.setUpdatedOn (LocalDateTime.now ());

        walletRepository.save (wallet);


        return transactionService.createNewTransaction (
                wallet.getOwner (),
                SMART_WALLET_LTD,
                wallet.getId ().toString (),
                amount,
                wallet.getBalance (),
                wallet.getCurrency (),
                TransactionType.DEPOSIT,
                TransactionStatus.SUCCEEDED,
                "Top Up %.2f".formatted (amount.doubleValue ()),
                null);
    }





    private Wallet getWalletById(UUID walletId) {
        return walletRepository.findById (walletId)
                .orElseThrow (() -> new DomainException ("Wallet with id [%s] does not exist."
                        .formatted (walletId), HttpStatus.BAD_REQUEST));
    }





    //method
    private Wallet initializeNewWallet(User user) {

        return Wallet.builder ()
                .owner (user)
                .status (WalletStatus.ACTIVE)
                .balance (new BigDecimal ("20.00"))
                .currency (Currency.getInstance ("EUR"))
                .createdOn (LocalDateTime.now ())
                .updatedOn (LocalDateTime.now ())
                .build ();
    }


}