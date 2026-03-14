package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;
import util.Validation;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    // Name validation
    private final Validation<String> validateName = name -> {
        if (name == null || name.isBlank())
            throw new ValidationException("Name is required");
    };

    // Email validation
    private final Validation<String> validateEmail = email -> {
        if (email == null || email.isBlank() || !email.contains("@"))
            throw new ValidationException("Invalid email");
    };

    // Account type validation
    private final Validation<String> validateType = type -> {
        if (type == null || !(type.equalsIgnoreCase("SAVINGS") || type.equalsIgnoreCase("CURRENT")))
            throw new ValidationException("Type must be SAVINGS or CURRENT");
    };

    // Amount validation
    private final Validation<Double> validateAmountPositive = amount -> {
        if (amount == null || amount <= 0)
            throw new ValidationException("Amount must be greater than 0");
    };

    // Account number validation
    private final Validation<String> validateAccountNumber = accountNumber -> {
        if (accountNumber == null || accountNumber.isBlank())
            throw new ValidationException("Account number is required");
    };

    @Override
    public String openAccount(String name, String email, String accountType) {

        validateName.validate(name);
        validateEmail.validate(email);
        validateType.validate(accountType);

        String customerId = UUID.randomUUID().toString();

        // Create customer
        Customer customer = new Customer(email, customerId, name);
        customerRepository.save(customer);

        // Generate account number
        String accountNumber = getAccountNumber();

        // Create account
        Account account = new Account(accountNumber, accountType, 0.0, customerId);
        accountRepository.save(account);

        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note) {

        validateAccountNumber.validate(accountNumber);
        validateAmountPositive.validate(amount);

        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found: " + accountNumber));

        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction(
                account.getAccountNumber(),
                amount,
                UUID.randomUUID().toString(),
                note,
                LocalDateTime.now(),
                Type.DEPOSIT
        );

        transactionRepository.add(transaction);
    }

    @Override
    public void withdraw(String accountNumber, Double amount, String note) {

        validateAccountNumber.validate(accountNumber);
        validateAmountPositive.validate(amount);

        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found: " + accountNumber));

        if (account.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient Balance");

        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction(
                account.getAccountNumber(),
                amount,
                UUID.randomUUID().toString(),
                note,
                LocalDateTime.now(),
                Type.WITHDRAW
        );

        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String fromAcc, String toAcc, Double amount, String note) {

        validateAccountNumber.validate(fromAcc);
        validateAccountNumber.validate(toAcc);
        validateAmountPositive.validate(amount);

        if (fromAcc.equals(toAcc))
            throw new ValidationException("Cannot transfer to the same account");

        Account from = accountRepository.findByNumber(fromAcc)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found: " + fromAcc));

        Account to = accountRepository.findByNumber(toAcc)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found: " + toAcc));

        if (from.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient Balance");

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        transactionRepository.add(new Transaction(
                from.getAccountNumber(),
                amount,
                UUID.randomUUID().toString(),
                note,
                LocalDateTime.now(),
                Type.TRANSFER_OUT
        ));

        transactionRepository.add(new Transaction(
                to.getAccountNumber(),
                amount,
                UUID.randomUUID().toString(),
                note,
                LocalDateTime.now(),
                Type.TRANSFER_IN
        ));
    }

    @Override
    public List<Transaction> getStatement(String accountNumber) {

        validateAccountNumber.validate(accountNumber);
        return transactionRepository.findByAccount(accountNumber)
                .stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String query) {

        String q = (query == null) ? "" : query.toLowerCase();

        return customerRepository.findAll()
                .stream()
                .filter(c -> c.getName().toLowerCase().contains(q))
                .flatMap(c -> accountRepository.findByCustomerId(c.getId()).stream())
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }

    // Generate Account Number
    private String getAccountNumber() {

        int size = accountRepository.findAll().size() + 1;
        return String.format("AC%06d", size);
    }
}
