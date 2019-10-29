package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.karolskolasinski.bquizgame.model.account.Account;
import pl.karolskolasinski.bquizgame.model.account.AccountPasswordResetRequest;
import pl.karolskolasinski.bquizgame.service.AccountService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(path = "/account/")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /*Register GET*/
    @GetMapping("/register")
    public String registrationForm(Model model, Account account) {
        model.addAttribute("newAccount", account);
        return "account/registration-form";
    }

    /*Register POST*/
    @PostMapping("/register")
    public String register(@Valid Account account, BindingResult result, Model model, String passwordConfirm) {
        if (result.hasErrors()) {
            return registrationError(model, account, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        if (accountService.existByEmail(account.getEmail())) {
            return registrationError(model, account, "Konto z takim emailem już istnieje.");
        }
        if (!account.getPassword().equals(passwordConfirm)) {
            return registrationError(model, account, "Hasła nie są identyczne.");
        }
        if (!accountService.register(account)) {
            return registrationError(model, account, "Użytkownik z taką nazwą już istnieje.");
        }
        return "redirect:/login";
    }

    private String registrationError(Model model, Account account, String message) {
        model.addAttribute("newAccount", account);
        model.addAttribute("errorMessage", message);
        return "account/registration-form";
    }

    @GetMapping("/myProfile")
    public String myProfile(Model model, Principal principal) {
        Optional<Account> accountOptional = accountService.findByUsername(principal.getName());
        if (accountOptional.isPresent()) {
            model.addAttribute("account", accountOptional.get());
            return "account/account-details";
        }
        return "redirect:/";
    }

    /*Reset password GET*/
    @GetMapping("/resetPassword/{accountId}")
    public String resetPassword(Model model, Principal principal, @PathVariable(name = "accountId") Long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);
        if (accountOptional.isPresent()) {
            if (accountOptional.get().getUsername().equals(principal.getName())) {
                model.addAttribute("account", accountOptional.get());
                return "account/account-resetpassword";
            }
        }
        return "redirect:/account/myProfile";
    }

    /*Reset password POST*/
    @PostMapping("/resetPassword")
    public String resetPassword(AccountPasswordResetRequest request) {
        accountService.resetPassword(request);
        return "redirect:/account/myProfile";
    }
}