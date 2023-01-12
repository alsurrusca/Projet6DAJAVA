package com.openclassromms.paymybuddy.ProjectPayMyBuddy.controller;

import com.openclassromms.paymybuddy.ProjectPayMyBuddy.DTO.BankDto;
import com.openclassromms.paymybuddy.ProjectPayMyBuddy.model.BankAccount;
import com.openclassromms.paymybuddy.ProjectPayMyBuddy.model.TransactionBank;
import com.openclassromms.paymybuddy.ProjectPayMyBuddy.model.User;
import com.openclassromms.paymybuddy.ProjectPayMyBuddy.service.BankAccountService;
import com.openclassromms.paymybuddy.ProjectPayMyBuddy.service.TransactionBankService;
import com.openclassromms.paymybuddy.ProjectPayMyBuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/bankTransaction")
public class TransactionBankController {
    @Autowired
    private UserService userService;


    Logger log = LoggerFactory.getLogger(TransactionBankController.class);

    @Autowired
    TransactionBankService transactionBankService;

    @GetMapping
    public String transactionBank(Model model, Principal user) {

        User userConnected = userService.getByEmail(user.getName());


        List<BankAccount> bankAccountList = userConnected.getBankAccountList();
        model.addAttribute("bankAccountList", bankAccountList);

        return "bankTransaction";
    }

    /**
     *
     * @param user
     * @param amount
     * @param bankAccount
     * @param redir
     * @return bankTransaction
     */

    @PostMapping
    public String depositMoneyToBank(Principal user, float amount, BankAccount bankAccount, RedirectAttributes redir) {

        User userConnected = userService.getByEmail(user.getName());


        if (amount > 0 && amount <= userConnected.getWallet()) {
            transactionBankService.depositToBank(userConnected, amount);
            redir.addFlashAttribute("banktransactionsuccess", "OK");
            log.info("Deposit Money to Bank Account SUCCESS ");
            return "redirect:/bankTransaction";
        } else {
            log.error("Deposit Money FAILED");
            redir.addFlashAttribute("erroramount", "KO");
            return "redirect:/bankTransaction";
        }

    }



}
