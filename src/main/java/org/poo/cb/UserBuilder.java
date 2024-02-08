package org.poo.cb;

public class UserBuilder {
    private User user;
    public UserBuilder withPortfolio(User user, Portfolio portfolio) {
        this.user = user;
        this.user.setPortfolio(portfolio);
        return this;
    }
    public User build() {
        return this.user;
    }

}
