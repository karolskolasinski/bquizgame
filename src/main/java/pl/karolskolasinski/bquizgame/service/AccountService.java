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

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRoleService accountRoleService;
    private final AccountRoleRepository accountRoleRepository;


    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountRoleService accountRoleService, AccountRoleRepository accountRoleRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRoleService = accountRoleService;
        this.accountRoleRepository = accountRoleRepository;
    }


    public boolean register(Account account) {
        if (accountRepository.existsByUsername(account.getUsername())) {
            return false;
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAccountRoles(accountRoleService.getDefaultRoles());
        accountRepository.save(account);

        return true;
    }

    public void remove(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            Account account = accountRepository.getOne(accountId);

            if (!account.isAdmin()) {
                accountRepository.delete(account);
            }
        }
    }


    public void resetPassword(AccountPasswordResetRequest request) {
        if (accountRepository.existsById(request.getAccountId())) {
            Account account = accountRepository.getOne(request.getAccountId());
            account.setPassword(passwordEncoder.encode(request.getResetPassword()));

            accountRepository.save(account);
        }
    }


    public void editRoles(Long accountId, HttpServletRequest request) {
        if (accountRepository.existsById(accountId)) {
            Account account = accountRepository.getOne(accountId);
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


    public List<Account> findAll() {
        return accountRepository.findAll();
    }


    public Account findById(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        return accountOptional.orElseGet(Account::new);
    }


    public Account findByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);

        return optionalAccount.orElseGet(Account::new);
    }


    public boolean existByEmail(String emial) {
        return accountRepository.existsByEmail(emial);
    }


    public Page<Account> getPage(PageRequest of) {
        return accountRepository.findAll(of);
    }

}
