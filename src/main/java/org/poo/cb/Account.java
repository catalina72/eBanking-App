package org.poo.cb;

import java.util.Stack;

public class Account {
    private String currency;
    private Double value;
    private User owner;
    Observer observer = new Observer(this);
    public Account(String currency, User owner) {
        this.currency = currency;
        this.value = Double.parseDouble("0");
        this.owner = owner;
    }
    public void attach(Observer observer) {
        this.observer = observer;
    }
    protected void notifyObserver() {
        observer.update();
    }
    public String getCurrency() {
        return currency;
    }
    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
        notifyObserver();
    }
    public User getOwner() {
        return owner;
    }
}

class GBPAccount extends Account {
    public GBPAccount(User owner) {
        super("GBP", owner);
    }
}
class EURAccount extends Account {
    public EURAccount(User owner) {
        super("EUR", owner);
    }
}
class JPYAccount extends Account {
    public JPYAccount(User owner) {
        super("JPY", owner);
    }
}
class CADAccount extends Account {
    public CADAccount(User owner) {
        super("CAD", owner);
    }
}
class USDAccount extends Account {
    public USDAccount(User owner) {
        super("USD", owner);
    }
}

class AccountFactory{
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

class Observer{
    Account subject;
    public Observer(Account subject) {
        this.subject = subject;
        if (this.subject != null) {
            this.subject.attach(this);
        }
    }
    public void update() {
        subject.getOwner().getNotification("There have been modifications to your account balance");
    }
}

interface Command{
    void execute();
    void undo();
}

class AddMoneyCommand implements Command{
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

class ExchangeMoneyCommand implements Command{
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

class PremiumExchange implements Command {
    protected Account sourceAccount;
    protected Account destinationAccount;
    protected Double exchangeRate;
    protected Double amount;
    protected Double oldSrcVal;
    protected Double oldDestVal;
    public PremiumExchange(Account sourceAccount, Account destinationAccount, Double exchangeRate, Double amount) {
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
        sourceAccount.setValue(sourceValue - amount * exchangeRate);
        destinationAccount.setValue(destinationValue + (amount));
    }

    @Override
    public void undo() {
        sourceAccount.setValue(oldSrcVal);
        destinationAccount.setValue(oldDestVal);
    }
}

class TransferMoneyCommand implements Command{
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

class BuyStocksCommand implements Command{
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

class PremiumStocks extends BuyStocksCommand {
    public PremiumStocks(Account stockAccount, String stockName, int quantity, Double stockPrice) {
        super(stockAccount, stockName, quantity, stockPrice);
    }

    @Override
    public void execute() {
        for(String stockName : EBankingApp.recommendedStocks) {
            if(stockName.equals(this.stockName)) {
                Double currentValue = stockAccount.getValue();
                stockAccount.setValue(currentValue - (0.95 * quantity * stockPrice));
                return;
            }
        }
        super.execute();
    }
}

class CommandHistory{
    private Stack<Command> commandHistory;
    public CommandHistory() {
        this.commandHistory = new Stack<>();
    }
    public void executeCommand(Command command) {
        command.execute();
        commandHistory.push(command);
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
        }
    }
}
