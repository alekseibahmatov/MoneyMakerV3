package models;

public class TaskItem {
    private final String name;
    private final boolean isNeededToBeDeposited, isNeededToBeTaken;
    private int amount, minAmount = -1;

    public TaskItem(String name, int amount, boolean isNeededToBeDeposited, boolean isNeededToBeTaken) {
        this.name = name;
        this.amount = amount;
        this.isNeededToBeDeposited = isNeededToBeDeposited;
        this.isNeededToBeTaken = isNeededToBeTaken;
    }

    public TaskItem(String name, int amount, int minAmount, boolean isNeededToBeDeposited, boolean isNeededToBeTaken) {
        this.name = name;
        this.amount = amount;
        this.minAmount = minAmount;
        this.isNeededToBeDeposited = isNeededToBeDeposited;
        this.isNeededToBeTaken = isNeededToBeTaken;
    }

    public TaskItem(String name, boolean isNeededToBeDeposited, boolean isNeededToBeTaken) {
        this.name = name;
        this.isNeededToBeDeposited = isNeededToBeDeposited;
        this.isNeededToBeTaken = isNeededToBeTaken;
    }

    public String getName() {
        return name;
    }

    public boolean isNeededToBeDeposited() {
        return isNeededToBeDeposited;
    }

    public boolean isNeededToBeTaken() {
        return isNeededToBeTaken;
    }

    public int getAmount() {
        return amount;
    }

    public int getMinAmount() {
        return minAmount;
    }
}
