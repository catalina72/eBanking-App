package org.poo.cb;

public class BuyStocksCommand implements Command{
    protected  Account stockAccount;
    protected String stockName;
    protected int quantity;
    protected Double stockPrice;

    public BuyStocksCommand(Account stockAccount, String stockName, int quantity, Double stockPrice) {
        this.stockAccount = stockAccount;
        this.stockName = stockName;
        this.quantity = quantity;
        this.stockPrice = stockPrice;
    }

    @Override
    public void execute() {
        Double currentValue = stockAccount.getValue();
        stockAccount.setValue(currentValue - (quantity * stockPrice));
    }

    @Override
    public void undo() {
        Double currentValue = stockAccount.getValue();
        stockAccount.setValue(currentValue + (quantity * stockPrice));
    }
}