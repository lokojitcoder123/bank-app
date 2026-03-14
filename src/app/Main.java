package app;

import service.BankService;
import service.impl.BankServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();
        boolean running = true;
        System.out.println("Welcome to Console Bank");
        while (running) {
            System.out.println("""
                1) Open Account
                2) Deposit
                3) Withdraw
                4) Transfer
                5) Account Statement
                6) List Accounts
                7) Search Accounts by Customer Name
                0) Exit
            """);
            System.out.print("CHOOSE: ");
            String choice = readLine(scanner);
            if (choice == null) {
                System.out.println("No input. Exiting.");
                break;
            }
            choice = choice.trim();
            System.out.println("CHOICE: " + choice);

            try {
                switch (choice) {
                    case "1" -> openAccount(scanner, bankService);
                    case "2" -> deposit(scanner, bankService);
                    case "3" -> withdraw(scanner, bankService);
                    case "4" -> transfer(scanner, bankService);
                    case "5" -> statement(scanner, bankService);
                    case "6" -> listAccounts(scanner, bankService);
                    case "7" -> searchAccounts(scanner, bankService);
                    case "0" -> running = false;
                    default -> System.out.println("Invalid choice. Please select a listed option.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }


    }

    private static void openAccount(Scanner scanner, BankService bankService) {
        System.out.println("Customer name: ");
        String name = readLine(scanner);
        if (name == null) return;
        name = name.trim();
        System.out.println("Customer email: ");
        String email = readLine(scanner);
        if (email == null) return;
        email = email.trim();
        System.out.println("Account Type (SAVINGS/CURRENT): ");
        String type = readLine(scanner);
        if (type == null) return;
        type = type.trim();
        System.out.println("Initial deposit (optional, blank for 0): ");
        Double initial = readDouble(scanner, true, 0.0);
        if (initial == null) return;
        String accountNumber = bankService.openAccount(name,email,type);
        if (initial > 0)
            bankService.deposit(accountNumber, initial, "Initial Deposit");
        System.out.println("Account opened: " + accountNumber);
    }

    private static void deposit(Scanner scanner, BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber = readLine(scanner);
        if (accountNumber == null) return;
        accountNumber = accountNumber.trim();
        System.out.println("Amount: ");
        Double amount = readDouble(scanner, false, 0.0);
        if (amount == null) return;
        bankService.deposit(accountNumber, amount, "Deposit");
        System.out.println("Deposited");
    }

    private static void withdraw(Scanner scanner, BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber = readLine(scanner);
        if (accountNumber == null) return;
        accountNumber = accountNumber.trim();
        System.out.println("Amount: ");
        Double amount = readDouble(scanner, false, 0.0);
        if (amount == null) return;
        bankService.withdraw(accountNumber, amount, "Withdrawal");
        System.out.println("Withdrawn");
    }

    private static void transfer(Scanner scanner, BankService bankService) {
        System.out.println("From Account: ");
        String from = readLine(scanner);
        if (from == null) return;
        from = from.trim();
        System.out.println("To Account: ");
        String to = readLine(scanner);
        if (to == null) return;
        to = to.trim();
        System.out.println("Amount: ");
        Double amount = readDouble(scanner, false, 0.0);
        if (amount == null) return;
        bankService.transfer(from, to, amount, "Transfer");
    }

    private static void statement(Scanner scanner, BankService bankService) {
        System.out.println("Account number: ");
        String account = readLine(scanner);
        if (account == null) return;
        account = account.trim();
        bankService.getStatement(account).forEach(t -> {
            System.out.println(t.getTimestamp() + " | " + t.getType() + " | " + t.getAmount() + " | " + t.getNote());
        });
    }

    private static void listAccounts(Scanner scanner, BankService bankService) {
        bankService.listAccounts().forEach(a -> {
            System.out.println(a.getAccountNumber() + " | " + a.getAccountType() + " | " + a.getBalance());
        });
    }

    private static void searchAccounts(Scanner scanner, BankService bankService) {
        System.out.println("Customer name contains: ");
        String q = readLine(scanner);
        if (q == null) return;
        q = q.trim();
        bankService.searchAccountsByCustomerName(q).forEach(account ->
                System.out.println(account.getAccountNumber() + " | " + account.getAccountType() + " | " + account.getBalance())
        );
    }

    private static String readLine(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine();
    }

    private static Double readDouble(Scanner scanner, boolean allowBlank, double defaultValue) {
        String input = readLine(scanner);
        if (input == null) return null;
        input = input.trim();
        if (allowBlank && input.isBlank()) return defaultValue;
        try {
            return Double.valueOf(input);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number. Please try again.");
            return null;
        }
    }

}
