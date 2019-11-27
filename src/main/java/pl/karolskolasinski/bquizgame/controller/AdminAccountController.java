package pl.karolskolasinski.bquizgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.karolskolasinski.bquizgame.model.account.Account;
import pl.karolskolasinski.bquizgame.service.AccountRoleService;
import pl.karolskolasinski.bquizgame.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping(path = "/admin/")
public class AdminAccountController {

    private AccountService accountService;
    private AccountRoleService accountRoleService;

    @Autowired
    public AdminAccountController(AccountService accountService, AccountRoleService accountRoleService) {
        this.accountService = accountService;
        this.accountRoleService = accountRoleService;
    }

    /*List users*/ //todo: paginacja
    @GetMapping("/listUsers")
    public String getUserList(Model model) {
        model.addAttribute("accounts", accountService.findAll());
        return "account/account-list";
    }

    /*Remove user*/
    @GetMapping("/remove/{accountId}")
    public String remove(@PathVariable(name = "accountId") Long accountId) {
        accountService.remove(accountId);
        return "redirect:/admin/listUsers";
    }

    /*Edit roles GET*/
    @GetMapping("/editRoles/{accountId}")
    public String editRoles(Model model, @PathVariable(name = "accountId") Long accountId) {
        Account account = accountService.findById(accountId);
        if (account.getId() != null) {
            model.addAttribute("roles", accountRoleService.getAll());
            model.addAttribute("account", account);
            return "account/account-roles";
        }
        return "redirect:/admin/listUsers";
    }

    /*Edit roles POST*/
    @PostMapping("/editRoles")
    public String editRoles(Long accountId, HttpServletRequest request) {
        accountService.editRoles(accountId, request);
        return "redirect:/admin/listUsers";
    }
}
