package app.web;

import app.transaction.model.Transaction;
import app.user.model.User;
import app.user.service.UserService;
import app.wallet.service.WalletService;
import app.web.dto.TransferRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/transfers")
public class TransferController {


    private final UserService userService;
    private final WalletService walletService;


    @Autowired
    public TransferController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }


    @GetMapping
    public ModelAndView getTransferPage (){

        User user = userService.getById (UUID.fromString ("559748e4-acaa-47ea-9456-6ec78e4a02bb"));

        ModelAndView modelAndView = new ModelAndView ();
        modelAndView.addObject ("user", user);
        modelAndView.setViewName ("transfer");
        modelAndView.addObject ("transferRequest", TransferRequest.builder ().build ());

        return modelAndView;
    }

    @PostMapping
    public ModelAndView initiateTransfer (@Valid TransferRequest transferRequest, BindingResult bindingResult){

        User user = userService.getById (UUID.fromString ("559748e4-acaa-47ea-9456-6ec78e4a02bb"));

        if (bindingResult.hasErrors ()){

            ModelAndView modelAndView = new ModelAndView ();
            modelAndView.addObject ("user", user);
            modelAndView.setViewName ("transfer");
            modelAndView.addObject ("transferRequest", transferRequest);

            return modelAndView;
        }

        Transaction transaction = walletService.transferFunds (user, transferRequest);

        return new ModelAndView ("redirect:/transactions/" + transaction.getId ());
    }
}
