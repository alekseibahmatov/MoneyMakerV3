package models;

public class NeedToBuyItem {
    private String name, checkName;
    private int itemID, itemQ, itemMinQ;

    public NeedToBuyItem(String name, int itemID, int itemQ, int itemMinQ, String checkName) {
        this.name = name;
        this.itemID = itemID;
        this.itemQ = itemQ;
        this.itemMinQ = itemMinQ;
        this.checkName = checkName;
    }

    public String getName() {
        return name;
    }

    public String getCheckName() {
        return checkName;
    }

    public int getItemID() {
        return itemID;
    }

    public int getItemQ() {
        return itemQ;
    }

    public int getItemMinQ() {
        return itemMinQ;
    }
}