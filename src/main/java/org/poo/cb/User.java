package org.poo.cb;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private List<User> friends;
    private Portfolio portfolio;
    private boolean premium;
    private List<String> notifications;
    public User(String email, String firstName, String lastName, String address) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.friends = new ArrayList<User>();
        premium = false;
        notifications = new ArrayList<>();
    }
    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
    public String getEmail() {
        return email;
    }
    public boolean isPremium() { return premium; }
    public void printJson() {
        System.out.print("{\"email\":\"" +  email + "\",\"firstname\":\"" + firstName + "\",\"lastname\":\"" + lastName +
                "\",\"address\":\"" + address + "\",\"friends\":[");
        if(friends != null)
            for(int i = 0; i < friends.size(); i++) {
                System.out.print("\"" + friends.get(i).email + "\"");
                if(i != friends.size() - 1)
                    System.out.print(",");
            }
        System.out.println("]}");
    }
    public void buyPremium() {
        premium = true;
    }

    @Override
    public boolean equals(Object obj) {
        if(email != null)
            return email.equals(((User)obj).email);
        return false;
    }

    public boolean isFriend(User user) {
        if(friends == null)
            return false;
        for(User friend : friends) {
            if(user.equals(friend))
                return true;
        }
        return false;
    }
    public void addFriend(User user) {
        friends.add(user);
    }
    public Portfolio getPortfolio() {
        return portfolio;
    }
    public void getNotification(String notification) {
        notifications.add(notification);
    }
}
