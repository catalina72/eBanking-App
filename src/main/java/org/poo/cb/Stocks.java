package org.poo.cb;

import java.util.List;

class Stocks {
    private String companyName;
    private List<Double> values;
    private int amount;
    Stocks(String name, int amount) {
        this.companyName = name;
        this.amount = amount;
        this.values = EBankingApp.getStocks().get(name);
    }
    public String getCompanyName() {
        return companyName;
    }

    public int getAmount() {
        return amount;
    }
}