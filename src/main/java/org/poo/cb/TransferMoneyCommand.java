package org.poo.cb;

public class TransferMoneyCommand implements Command {
    private Account sourceAccount;
    private Account destinationAccount;
    private Double amount;

    public TransferMoneyCommand(Account sourceAccount, Account destinationAccount, Double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    @Override
    public void execute() {
        Double sourceValue = sourceAccount.getValue();
        Double destinationValue = destinationAccount.getValue();

        sourceAccount.setValue(sourceValue - amount);
        destinationAccount.setValue(destinationValue + amount);
    }
    public void undo() {
        Double sourceValue = sourceAccount.getValue();
        Double destinationValue = destinationAccount.getValue();

        sourceAccount.setValue(sourceValue + amount);
        destinationAccount.setValue(destinationValue - amount);
    }
}

