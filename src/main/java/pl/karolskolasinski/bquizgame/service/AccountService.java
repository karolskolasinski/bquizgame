package pl.karolskolasinski.bquizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.karolskolasinski.bquizgame.model.account.Account;
import pl.karolskolasinski.bquizgame.model.account.AccountPasswordResetRequest;
import pl.karolskolasinski.bquizgame.model.account.AccountRole;
import pl.karolskolasinski.bquizgame.repository.AccountRepository;
import pl.karolskolasinski.bquizgame.repository.AccountRoleRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private AccountRoleService accountRoleService;
    private AccountRoleRepository accountRoleRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountRoleService accountRoleService, AccountRoleRepository accountRoleRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRoleService = accountRoleService;
        this.accountRoleRepository = accountRoleRepository;
    }

    /*Register*/
    public boolean register(Account account) {
        if (accountRepository.existsByUsername(account.getUsername())) {
            return false;
        }
        //szyfrowanie hasła
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAccountRoles(accountRoleService.getDefaultRoles());
        accountRepository.save(account);
        return true;
    }

    /*Remove*/
    public void remove(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            Account account = accountRepository.getOne(accountId);
            if (!account.isAdmin()) {
                accountRepository.delete(account);
            }
        }
    }

    /*Reset password*/
    public void resetPassword(AccountPasswordResetRequest request) {
        if (accountRepository.existsById(request.getAccountId())) {
            Account account = accountRepository.getOne(request.getAccountId());

            account.setPassword(passwordEncoder.encode(request.getResetPassword()));
            accountRepository.save(account);
        }
    }

    /*Edit roles*/
    public void editRoles(Long accountId, HttpServletRequest request) {
        if (accountRepository.existsById(accountId)) {
            Account account = accountRepository.getOne(accountId);
            // kluczem w form parameters jest nazwa parametru th:name
            Map<String, String[]> formParameters = request.getParameterMap();
            Set<AccountRole> newCollectionOfRoles = new HashSet<>();
            for (String roleName : formParameters.keySet()) {
                String[] values = formParameters.get(roleName);
                if (values[0].equals("on")) {
                    Optional<AccountRole> accountRoleOptional = accountRoleRepository.findByName(roleName);
                    accountRoleOptional.ifPresent(newCollectionOfRoles::add);
                }
            }
            account.setAccountRoles(newCollectionOfRoles);
            accountRepository.save(account);
        }
    }

    /*Find*/
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public boolean existByEmail(String emial) {
        return accountRepository.existsByEmail(emial);
    }

    public Page<Account> getPage(PageRequest of) {
        return accountRepository.findAll(of);
    }
}