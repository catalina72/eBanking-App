package org.poo.cb;

import java.io.*;
import java.util.*;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

import static org.poo.cb.EBankingApp.exchangeRates;
import static org.poo.cb.EBankingApp.stockValue;

public class Main {
    public static void main(String[] args) {
        if(args == null) {
            System.out.println("Running Main");
        } else if(args.length == 3) {
            // 0 exchange rates
            // 1 stockvalues
            // 2 commands
            EBankingApp app = EBankingApp.getInstance();

            try {
                FileReader fr = new FileReader("src/main/resources/" + args[0]);
                CSVReader reader = new CSVReaderBuilder(fr).build();
                List<String[]> myEntries = reader.readAll();
                boolean firstLine = true;
                for (String[] nextLine : myEntries) {
                    if(firstLine) {
                        firstLine = false;
                        continue;
                    }
                    String baseCurrency = nextLine[0];
                    Map<String, Double> rates = new HashMap<>();
                    for(int i = 1; i < nextLine.length; i++) {
                        String targetCurrency = myEntries.get(0)[i];
                        double exchangeRate = Double.parseDouble(nextLine[i]);
                        rates.put(targetCurrency, exchangeRate);
                    }
                    exchangeRates.put(baseCurrency, rates);
                }
            } catch (IOException | CsvException e) {
                e.printStackTrace();
            }

            try {
                FileReader fr = new FileReader("src/main/resources/" + args[1]);
                CSVReader reader = new CSVReaderBuilder(fr).withSkipLines(1).build();
                List<String[]> myEntries = reader.readAll();
                for (String[] nextLine : myEntries) {
                    String companyName = nextLine[0];
                    List <Double> values = new ArrayList<>();
                    for(int i = 1; i < nextLine.length; i++) {
                        Double stockValue = Double.parseDouble(nextLine[i]);
                        values.add(stockValue);
                    }
                    stockValue.put(companyName, values);
                }
            } catch (IOException | CsvException e) {
                e.printStackTrace();
            }

            try {
                FileReader fr = new FileReader("src/main/resources/" + args[2]);
                BufferedReader br = new BufferedReader(fr);
                String line = null;
                try {
                    line = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    while(line != null) {
                        String[] tokens = line.split(" ");
                        if(tokens[0].equals("CREATE") && tokens[1].equals("USER")) {
                            String email = tokens[2];
                            String firstName = tokens[3];
                            String lastName = tokens[4];
                            String address = getAddress(tokens);
                            try {
                                app.createUser(email, firstName, lastName, address);
                            } catch (EmailAlreadyExistsException e) {}

                        } else if(tokens[0].equals("LIST") && tokens[1].equals("USER")) {
                            String email = tokens[2];
                            try {
                                app.listUser(email);
                            } catch (UserDoesntExistException e) {}
                        } else if(tokens[0].equals("ADD") && tokens[1].equals("FRIEND")) {
                            String user = tokens[2];
                            String friend = tokens[3];
                            try {
                                app.addFriend(user, friend);
                            } catch (UserDoesntExistException | AlreadyFriendException e) {}
                        } else if(tokens[0].equals("ADD") && tokens[1].equals("ACCOUNT")) {
                            String email = tokens[2];
                            String currency = tokens[3];
                            try {
                                app.addAccount(email, currency);
                            } catch (AccountAlreadyExistsException e) {}
                        } else if(tokens[0].equals("ADD") && tokens[1].equals("MONEY")) {
                            String email = tokens[2];
                            String currency = tokens[3];
                            Double value = Double.parseDouble(tokens[4]);
                            app.addMoney(email, currency, value);
                        } else
                            //System.out.println(tokens[0] + " " + tokens[1]);
                            if(tokens[0].equals("LIST") && tokens[1].equals("PORTFOLIO")) {

                            String email = tokens[2];
                            try {
                                app.listPortofolio(email);
                            } catch(UserDoesntExistException e) {}
                        } else if(tokens[0].equals("EXCHANGE") && tokens[1].equals("MONEY")) {
                            String email = tokens[2];
                            String sourceCurrency = tokens[3];
                            String destinationCurrency = tokens[4];
                            Double amount = Double.parseDouble(tokens[5]);
                            try{
                                app.exchangeMoney(email, sourceCurrency, destinationCurrency, amount);
                            } catch(InsufficientAmountException e) {}
                        } else if(tokens[0].equals("TRANSFER") && tokens[1].equals("MONEY")) {
                            String email = tokens[2];
                            String friendEmail = tokens[3];
                            String currency = tokens[4];
                            Double amount = Double.parseDouble(tokens[5]);
                            try{
                                app.transferMoney(email, friendEmail, currency, amount);
                            } catch (InsufficentAmountTransferException | NotFriendsException e) {}
                        } else if(tokens[0].equals("BUY") && tokens[1].equals("STOCKS")) {
                            String email = tokens[2];
                            String companyName = tokens[3];
                            Integer noOfStocks = Integer.parseInt(tokens[4]);
                            try{
                                app.buyStocks(email, companyName, noOfStocks);
                            } catch (InsufficientAmountStocksException e) {}

                        } else if(tokens[0].equals("RECOMMEND") && tokens[1].equals("STOCKS")) {
                                app.recommendStocks();
                        } else if(tokens[0].equals("BUY") && tokens[1].equals("PREMIUM")) {
                                String email = tokens[2];
                                try {
                                    app.buyPremium(email);
                                } catch (UserDoesntExistException | InsufficientPremiumException e) {
                                }
                            }
                                try {
                            line = br.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            app.reset();
        }
    }
    private static String getAddress(String[] arg) {
        String address = "";
        for(int i = 5; i < arg.length; i++) {
            address += arg[i];
            if(i != arg.length - 1)
                address += " ";
        }
        return address;
    }
}