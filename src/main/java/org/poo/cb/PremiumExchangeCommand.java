package org.poo.cb;

public class PremiumExchangeCommand implements Command {
    private final ExchangeMoneyCommand baseCommand;

    public PremiumExchangeCommand(ExchangeMoneyCommand baseCommand) {
        this.baseCommand = baseCommand;
    }

    @Override
    public void execute() {
        Double sourceValue = baseCommand.sourceAccount.getValue();
        Double destinationValue = baseCommand.destinationAccount.getValue();
        baseCommand.oldSrcVal = sourceValue;
        baseCommand.oldDestVal = destinationValue;
        baseCommand.sourceAccount.setValue(sourceValue - baseCommand.amount * baseCommand.exchangeRate);
        baseCommand.destinationAccount.setValue(destinationValue + (baseCommand.amount));
    }

    @Override
    public void undo() {
        baseCommand.undo();
    }
}
