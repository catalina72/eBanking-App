package org.poo.cb;

public class Observer{
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
