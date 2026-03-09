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
                case "2" -> deposit(scanner);
                case "3" -> withdraw(scanner);
                case "4" -> transfer(scanner);
                case "5" -> statement(scanner);
                case "6" -> listAccounts(scanner);
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
        bankService.openAccount(name,email,type);
    }

    private void deposit(Scanner scanner) {
    }

    private void withdraw(Scanner scanner) {
    }

    private void transfer(Scanner scanner) {
    }

    private void statement(Scanner scanner) {
    }

    private void listAccounts(Scanner scanner) {
    }

    private void searchAccounts(Scanner scanner) {
    }
    


}