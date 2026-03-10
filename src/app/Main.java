package app;

import service.BankService;
import service.impl.BankServiceImpl;

import java.util.Scanner;

public class Main {
    public void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();
        boolean running = true;
        System.out.println("Welcome to console Bank");
        while (running) {
            System.out.println("""
                             1) Open account
                             2) Deposit
                             3)Withdraw
                             4) Transfer
                             5) Account Statement
                             6)List Accounts
                             7)Search Accounts by coustomer name
                             0) Exit
                    """);
            System.out.println("CHOOSE: ");
            String choise = scanner.nextLine().trim();
            System.out.println("CHOISE: " + choise);


            switch(choise){
                case "1" -> openAccount(scanner, bankService);
                case "2" -> deposit(scanner, bankService);
                case "3" -> withdraw(scanner);
                case "4" -> transfer(scanner);
                case "5" -> statement(scanner);
                case "6" -> listAccounts(scanner,bankService);
                case "7" -> searchAccounts(scanner);
                case "0" -> running = false;
            }
        }
    }

    private void openAccount(Scanner scanner , BankService bankService) {
        System.out.println("Coustomer name: ");
        String name = scanner.next().trim();
        System.out.println("Coustomer email: ");
        String email = scanner.next().trim();
        System.out.println("Account Type (SAVINGS/CURRENT): ");
        String type = scanner.next().trim();
        System.out.println("Initial deposit (optional , blank for 0): ");
        String amountStr = scanner.next().trim();
        Double initial = Double.valueOf(amountStr);
        String accountNumber = bankService.openAccount(name,email,type);
        if(initial > 0)
            bankService.deposit(accountNumber, initial, "Inital Deposit");
         System.out.println("Account oppend: " + accountNumber);
    }

    private void deposit(Scanner scanner , BankService bankService) {
        System.out.println("Account number:  ");
        String accountNumber = scanner.nextLine().trim();
        System.out.println("Ammount: ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.deposit(accountNumber , amount ,"Deposit");
        System.out.println("Deposit");



    }

    private void withdraw(Scanner scanner) {
    }

    private void transfer(Scanner scanner) {
    }

    private void statement(Scanner scanner) {
    }

    private void listAccounts(Scanner scanner , BankService bankService) {
          bankService.listaccounts().forEach(a -> {
              System.out.println(a.getAccountNumber() + "  |  " + a.getAccountType() +"  |  " + a.getBalance());
                  }

          );
    }

    private void searchAccounts(Scanner scanner) {
    }
    


}