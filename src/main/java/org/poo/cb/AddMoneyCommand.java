package org.poo.cb;

public class AddMoneyCommand implements Command{
    private Account account;
    private Double amount;

    public AddMoneyCommand(Account account, Double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() {
        Double currentValue = account.getValue();
        account.setValue(currentValue + amount);
    }
    public void undo() {
        Double currentValue = account.getValue();
        account.setValue(currentValue - amount);
    }
}
