package org.poo.cb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EBankingApp {
    private static EBankingApp instance;
    private List<User> users;
    private CommandHistory commandHistory = new CommandHistory();
    public static Map<String, List<Double>> stockValue = new HashMap<>();
    public static Map<String, Map<String, Double>> exchangeRates = new HashMap<>();
    public static List<String> recommendedStocks = new ArrayList<>();
    private EBankingApp() {
        users = new ArrayList<User>();
    }
    public static EBankingApp getInstance() {
        if(instance == null) {
            instance = new EBankingApp();
        }
        return instance;
    }
    public static Map<String, List<Double>> getStocks() {
        return stockValue;
    }
    public void reset() {
        users.clear();
        stockValue.clear();
        recommendedStocks.clear();
    }
    public void createUser(String email, String firstName, String lastName, String address) throws EmailAlreadyExistsException {
        if(findUser(email) != null) {
            throw new EmailAlreadyExistsException(email);
        }
        User user = new User(email, firstName, lastName, address);
        user = new UserBuilder().withPortfolio(user, new Portfolio(user)).build();
        users.add(user);
    }
    private User findUser (String email) {
        for(User user : users) {
            if(user.getEmail().equals(email))
                return user;
        }
        return null;
    }
    public void listUser(String email) throws UserDoesntExistException {
        User user = findUser(email);
        if(user == null) {
            throw new UserDoesntExistException(email);
        }
        user.printJson();
    }
    public void addFriend(String userEmail, String friendEmail) throws UserDoesntExistException, AlreadyFriendException{
        User user = findUser(userEmail);
        if(user == null) {
            throw new UserDoesntExistException(userEmail);
        }
        User friend = findUser(friendEmail);
        if(friend == null)
            throw new UserDoesntExistException(friendEmail);
        if(user.isFriend(friend))
            throw new AlreadyFriendException(friendEmail);
        user.addFriend(friend);
        friend.addFriend(user);
    }
    public void addAccount(String email, String currency) throws AccountAlreadyExistsException{
        User user = findUser(email);
        if(user.getPortfolio().hasAccount(currency)) {
            throw new AccountAlreadyExistsException(currency);
        }
        user.getPortfolio().addAccount(currency);
    }
    public void addMoney(String email, String currency, Double value){
        User user = findUser(email);
        Account account = user.getPortfolio().findAccount(currency);
        if(account != null) {
            AddMoneyCommand addMoney = new AddMoneyCommand(account, value);
            commandHistory.executeCommand(addMoney);
        }
    }
    public void listPortfolio(String email) throws UserDoesntExistException {
        User user = findUser(email);
        if(user == null)
            throw new UserDoesntExistException(email);
        user.getPortfolio().printJson();
    }

    public void exchangeMoney(String email, String src, String dest, Double amount) throws InsufficientAmountException{
        User user = findUser(email);
        Account srcAcc = user.getPortfolio().findAccount(src);
        Account destAcc = user.getPortfolio().findAccount(dest);
        if(amount > srcAcc.getValue()) {
            throw new InsufficientAmountException(src);
        }
        Double rate = exchangeRates.get(dest).get(src);
        Command exchangeMoney = new ExchangeMoneyCommand(srcAcc, destAcc, rate, amount);
        if(user.isPremium())
            exchangeMoney = new PremiumExchangeCommand((ExchangeMoneyCommand) exchangeMoney);
        commandHistory.executeCommand(exchangeMoney);
    }

    public void transferMoney(String email, String friendEmail, String currency, Double amount) throws InsufficentAmountTransferException, NotFriendsException {
        User user = findUser(email);
        User friend = findUser(friendEmail);
        Account srcAcc = user.getPortfolio().findAccount(currency);
        Account destAcc = friend.getPortfolio().findAccount(currency);
        if(!user.isFriend(friend))
            throw new NotFriendsException(friendEmail);
        if(srcAcc.getValue() < amount)
            throw new InsufficentAmountTransferException(currency);
        TransferMoneyCommand transferMoney = new TransferMoneyCommand(srcAcc, destAcc, amount);
        commandHistory.executeCommand(transferMoney);
    }

    public void recommendStocks() {
        recommendedStocks = new ArrayList<>();
        for(Map.Entry<String, List<Double>> stock : stockValue.entrySet()) {
            Double shortSMA = calculateSMA(stock.getValue().subList(5, 10));
            Double longSMA = calculateSMA(stock.getValue().subList(0, 10));

            if (shortSMA > longSMA) {
                recommendedStocks.add(stock.getKey());
            }
        }
        System.out.print("{\"stocksToBuy\":[");
        if(recommendedStocks != null)
            for(int i = 0; i < recommendedStocks.size(); i++) {
                System.out.print("\"" + recommendedStocks.get(i) + "\"");
                if(i != recommendedStocks.size() - 1)
                    System.out.print(",");
            }
        System.out.println("]}");
    }
    private Double calculateSMA(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).sum() / values.size();
    }
    public void buyStocks(String email, String companyName, Integer amount) throws InsufficientAmountStocksException{
        User user = findUser(email);
        Account acc = user.getPortfolio().findAccount("USD");
        Double rate = stockValue.get(companyName).getLast();
        if(acc.getValue() < rate * amount) {
            throw new InsufficientAmountStocksException();
        }
        Command buyStocks = new BuyStocksCommand(acc, companyName, amount, rate);
        if(user.isPremium())
            buyStocks = new PremiumStocksCommand((BuyStocksCommand) buyStocks);
        commandHistory.executeCommand(buyStocks);
        user.getPortfolio().buyStock(companyName, amount);
    }
    public void buyPremium(String email) throws UserDoesntExistException, InsufficientPremiumException {
        User user = findUser(email);
        if(user == null)
            throw new UserDoesntExistException(email);
        Account acc = user.getPortfolio().findAccount("USD");
        if(acc.getValue() < 100)
            throw new InsufficientPremiumException();
        acc.setValue(acc.getValue() - 100);
        user.buyPremium();
    }
}
