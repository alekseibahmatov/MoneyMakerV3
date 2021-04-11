package models;

import org.osbot.rs07.api.map.Area;

public class TaskItem {
    private final String name;
    private final boolean isNeededToBeDeposited, isNeededToBeTaken;
    private boolean isUsedForTransporting;
    private int amount, minAmount = -1;
    private  Area area;

    public TaskItem(String name, int amount, boolean isNeededToBeDeposited, boolean isNeededToBeTaken) {
        this.name = name;
        this.amount = amount;
        this.isNeededToBeDeposited = isNeededToBeDeposited;
        this.isNeededToBeTaken = isNeededToBeTaken;
    }

    public TaskItem(String name, int amount, boolean isNeededToBeDeposited, boolean isNeededToBeTaken, boolean isUsedForTransporting, Area area) {
        this.name = name;
        this.amount = amount;
        this.isNeededToBeDeposited = isNeededToBeDeposited;
        this.isNeededToBeTaken = isNeededToBeTaken;
        this.isUsedForTransporting = isUsedForTransporting;
        this.area = area;
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

    public boolean isUsedForTransporting() {
        return isUsedForTransporting;
    }

    public Area getArea() {
        return area;
    }
}
