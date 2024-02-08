package org.poo.cb;

public class AccountFactory{
    public static enum AccountType {
        EUR, GBP, JPY, CAD, USD
    }
    private static AccountFactory instance;

    private AccountFactory() {
    }

    public static AccountFactory getInstance() {
        if (instance == null) {
            instance = new AccountFactory();
        }
        return instance;
    }
    public Account createAccount(AccountType accountType, User owner) {
        switch (accountType) {
            case EUR -> {
                return new EURAccount(owner);
            }
            case GBP -> {
                return new GBPAccount(owner);
            }
            case JPY -> {
                return new JPYAccount(owner);
            }
            case CAD -> {
                return new CADAccount(owner);
            }
            case USD -> {
                return new USDAccount(owner);
            }
            case null, default -> {
                return null;
            }
        }
    }
}
