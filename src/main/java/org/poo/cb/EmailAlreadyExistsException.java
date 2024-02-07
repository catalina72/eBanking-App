package org.poo.cb;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(String email) {
        System.out.println("User with " + email + " already exists");
    }
}

class UserDoesntExistException extends Exception {
    public UserDoesntExistException(String email) {
        System.out.println("User with " + email + " doesn't exist");
    }
}

class AlreadyFriendException extends Exception {
    public AlreadyFriendException(String email) {
        System.out.println("User with " + email + " is already a friend");
    }
}

class AccountAlreadyExistsException extends Exception{
    public AccountAlreadyExistsException(String currency) {
        System.out.println("Account in currency " + currency + " already exists for user");
    }
}

class InsufficientAmountException extends Exception {
    public InsufficientAmountException(String currency) {
        System.out.println("Insufficient amount in account " + currency + " for exchange");
    }
}

class InsufficentAmountTransferException extends Exception {
    public InsufficentAmountTransferException(String currency) {
        System.out.println("Insufficient amount in account " + currency + " for transfer");
    }
}

class NotFriendsException extends Exception {
    public NotFriendsException(String email) {
        System.out.println("You are not allowed to transfer money to " + email);
    }
}

class InsufficientAmountStocksException extends Exception {
    public InsufficientAmountStocksException() {
        System.out.println("Insufficient amount in account for buying stock");
    }
}

class InsufficientPremiumException extends Exception {
    public InsufficientPremiumException() {
        System.out.println("Insufficient amount in account for buying premium option");
    }
}
