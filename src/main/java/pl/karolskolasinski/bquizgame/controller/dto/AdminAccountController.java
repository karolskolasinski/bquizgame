package pl.karolskolasinski.bquizgame.controller.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.karolskolasinski.bquizgame.model.account.Account;
import pl.karolskolasinski.bquizgame.service.AccountRoleService;
import pl.karolskolasinski.bquizgame.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping(path = "/admin/account/")
public class AdminAccountController {

    private AccountService accountService;
    private AccountRoleService accountRoleService;

    @Autowired
    public AdminAccountController(AccountService accountService, AccountRoleService accountRoleService) {
        this.accountService = accountService;
        this.accountRoleService = accountRoleService;
    }

    @GetMapping("/list")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String getUserList(Model model) {
        model.addAttribute("accounts", accountService.getAll());
        return "account-list";
    }

    @GetMapping("/remove")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String remove(@RequestParam(name = "accountId") Long accountId) {
        accountService.remove(accountId);

        return "redirect:/admin/account/list";
    }

    @GetMapping("/editRoles")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String editRoles(Model model, @RequestParam(name = "accountId") Long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);
        if (accountOptional.isPresent()) {
            model.addAttribute("roles", accountRoleService.getAll());
            model.addAttribute("account", accountOptional.get());

            return "account-roles";
        }
        return "redirect:/admin/account/list";
    }

    @PostMapping("/editRoles")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String editRoles(Long accountId, HttpServletRequest request) {
        accountService.editRoles(accountId, request);

        return "redirect:/admin/account/list";
    }
}