package org.poo.cb;

import java.util.Stack;

public class CommandHistory{
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
