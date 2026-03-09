package service.impl;

import domain.Account;
import repository.AccountRepository;
import service.BankService;
import java.util.UUID;

public class BankServiceImpl implements BankService {
    private final AccountRepository accountRepository = new AccountRepository();
    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId = UUID.randomUUID().toString();

        // CHANGE LATER -->
        String accountNumber = UUID.randomUUID().toString();
        Account a = new Account(accountNumber , accountType ,(double)0,customerId);
        //SAVE
        return "";
    }
}

