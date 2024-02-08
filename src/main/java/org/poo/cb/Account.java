package org.poo.cb;

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