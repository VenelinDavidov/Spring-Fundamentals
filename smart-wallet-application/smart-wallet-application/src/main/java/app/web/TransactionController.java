package app.web;

import app.transaction.model.Transaction;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/transactions")
public class TransactionController {


    private final TransactionService transactionService;




    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }





    @GetMapping
    public ModelAndView getAllTransactions (){

        List <Transaction> transactions = transactionService.getAllByOwnerId (UUID.fromString ("559748e4-acaa-47ea-9456-6ec78e4a02bb"));

        ModelAndView modelAndView = new ModelAndView ();
        modelAndView.addObject ("transactions", transactions);
        modelAndView.setViewName ("transactions");

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getTransactionById (@PathVariable UUID id){

        Transaction transaction = transactionService.getById (id);

        ModelAndView modelAndView = new ModelAndView ();
        modelAndView.setViewName ("transaction-result");
        modelAndView.addObject ("transaction", transaction);


        return modelAndView;
        }




}