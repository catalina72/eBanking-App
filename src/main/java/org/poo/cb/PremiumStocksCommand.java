package org.poo.cb;

public class PremiumStocksCommand implements Command{
    private final BuyStocksCommand baseCommand;

    public PremiumStocksCommand(BuyStocksCommand baseCommand) {
        this.baseCommand = baseCommand;
    }

    @Override
    public void execute() {
        for(String stockName : EBankingApp.recommendedStocks) {
            if(stockName.equals(this.baseCommand.stockName)) {
                Double currentValue = baseCommand.stockAccount.getValue();
                baseCommand.stockAccount.setValue(currentValue - (0.95 * baseCommand.quantity * baseCommand.stockPrice));
                return;
            }
        }
        baseCommand.execute();
    }

    @Override
    public void undo() {
        baseCommand.undo();
    }
}