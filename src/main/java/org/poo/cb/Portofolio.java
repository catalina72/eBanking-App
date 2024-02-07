package org.poo.cb;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;

public class Portofolio {
    private List<Account> accounts;
    private List<Stocks> stocks;
    private AccountFactory factory;
    private User owner;
    public Portofolio(User owner) {
        accounts = new ArrayList<>();
        stocks = new ArrayList<>();
        factory = AccountFactory.getInstance();
        this.owner = owner;
    }
    public boolean hasAccount (String currency) {
        for(Account account : accounts) {
            if(account.getCurrency().equals(currency))
                return true;
        }
        return false;
    }
    public void addAccount(String currency) {
        accounts.add(factory.createAccount(AccountFactory.AccountType.valueOf(currency), owner));
    }
    public Account findAccount(String currency) {
        for(Account account : accounts) {
            if(account.getCurrency().equals(currency)) {
                return account;
            }
        }
        return null;
    }
    public void printJson() {
        System.out.print("{\"stocks\":[");
        if(stocks != null)
            for(int i = 0; i < stocks.size(); i++) {
                System.out.print("{\"stockName\":\"" + stocks.get(i).getCompanyName() + "\",\"amount\":" + stocks.get(i).getAmount() + "}");
                if(i != stocks.size() - 1)
                    System.out.print(",");
            }
        System.out.print("],\"accounts\":[");
        if(accounts != null)
            for(int i = 0; i < accounts.size(); i++) {
                System.out.print("{\"currencyName\":\"" + accounts.get(i).getCurrency() + "\",\"amount\":\"" + String.format("%.2f", accounts.get(i).getValue()) + "\"}");
                if( i != accounts.size() - 1)
                    System.out.print(",");
            }
        System.out.println("]}");
    }
    public void buyStock(String companyName, Integer amount) {
        stocks.add(new Stocks(companyName, amount));
    }
}
