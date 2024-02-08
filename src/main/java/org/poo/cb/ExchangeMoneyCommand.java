package org.poo.cb;

public class ExchangeMoneyCommand implements Command{
    protected Account sourceAccount;
    protected Account destinationAccount;
    protected Double exchangeRate;
    protected Double amount;
    protected Double oldSrcVal;
    protected Double oldDestVal;
    public ExchangeMoneyCommand(Account sourceAccount, Account destinationAccount, Double exchangeRate, Double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.exchangeRate = exchangeRate;
        this.amount = amount;
    }

    @Override
    public void execute() {
        Double sourceValue = sourceAccount.getValue();
        Double destinationValue = destinationAccount.getValue();
        oldSrcVal = sourceValue;
        oldDestVal = destinationValue;
        if(amount * exchangeRate > 0.5 * sourceValue) {
            sourceAccount.setValue((sourceValue - 1.01 * amount * exchangeRate));
        } else
            sourceAccount.setValue(sourceValue - amount * exchangeRate);
        destinationAccount.setValue(destinationValue + (amount));
    }

    @Override
    public void undo() {
        sourceAccount.setValue(oldSrcVal);
        destinationAccount.setValue(oldDestVal);
    }
}
