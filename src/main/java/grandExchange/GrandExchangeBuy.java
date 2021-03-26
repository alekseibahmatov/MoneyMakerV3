package grandExchange;

import grandExchange.buy.GetBuyItems;
import grandExchange.utils.GrandExchangeManager;
import models.BuyItem;
import models.GrandExchangeBoxState;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;
import bank.utils.BankManager;
import utils.Sleep;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrandExchangeBuy extends MethodProvider {

    private Area grandExchangeArea = new Area(3169, 3488, 3161, 3486);

    private GetBuyItems getBuyItems = new GetBuyItems();

    private GrandExchange.Box boxes[] = {
            GrandExchange.Box.BOX_1,
            GrandExchange.Box.BOX_2,
            GrandExchange.Box.BOX_3,
            GrandExchange.Box.BOX_4,
            GrandExchange.Box.BOX_5,
            GrandExchange.Box.BOX_6,
            GrandExchange.Box.BOX_7,
            GrandExchange.Box.BOX_8
    };

    private int box_cords[][][] = {
            {{30, 139}, {107, 187}},
            {{149, 256}, {107, 187}},
            {{266, 372}, {107, 187}},
            {{382, 488}, {107, 187}},
            {{30, 139}, {228, 304}},
            {{149, 256}, {228, 304}},
            {{266, 372}, {228, 304}},
            {{382, 488}, {228, 304}}
    };

    private int currentBoxID = -1;
    private int state = 1;

    public long buyItems(int taskID, int step, BankManager bankManager, GrandExchangeManager grandExchangeManager) throws IOException, InterruptedException {
        currentBoxID = -1;

        state = 1;

        long goldAmount;

        List<BuyItem> items = null;

        while (true) {
            if (!grandExchangeArea.contains(myPosition())) getWalking().webWalk(grandExchangeArea);
            else {
                List<GrandExchangeBoxState> grandExchangeBoxStates = new ArrayList<>();

                currentBoxID = -1;

                if (state > 2) {
                    log("Analyzing grand exchange");
                    for (int i = 0; i < 8; i++) {
                        String status = getGrandExchange().getStatus(boxes[i]).toString();

                        if (status.equals("PENDING_BUY") || status.equals("COMPLETING_BUY") || status.equals("FINISHED_BUY")) {
                            GrandExchangeBoxState newState = new GrandExchangeBoxState();

                            newState.setStatus(status);
                            newState.setItemID(getGrandExchange().getItemId(boxes[i]));
                            newState.setSlot(i);

                            grandExchangeBoxStates.add(newState);
                        }
                    }
                }

                if (state == 1) {
                    log("Getting coins from bank");
                    bankManager.openBank();

                    if (!getInventory().isEmpty()) getBank().depositAll();

                    items = getBuyItems.getItems(taskID, step, getBank());

                    if (getBank().contains("Coins")) {

                        log("Bank has coins");

                        long goldTotalNeeded = 0, goldAvailable;

                        goldAvailable = getBank().getAmount("Coins");

                        for (BuyItem item : items) {
                            goldTotalNeeded += (long) item.getAmount() * item.getPrice();
                        }

                        log("Total coins needed: " + goldTotalNeeded);

                        if (goldAvailable < goldTotalNeeded * 1.1) return -1; // Not enough money
                        else {

                            log("Bank has enough coins");

                            state = 2;

                            getBank().withdrawAll("Coins");

                            sleep(random(300, 500));

                            getBank().close();

                            sleep(random(300, 500));
                        }
                    } else {
                        return -2; // Your bank account does not have money at all
                    }
                } else if (state == 2) {
                    log("Buying items");

                    grandExchangeManager.openGE();

                    sleep(random(300, 500));

                    for (BuyItem item : items) {
                        getGrandExchange().buyItem(item.getId(), item.getSearchTerm(), item.getPrice(), item.getAmount());

                        sleep(random(300, 500));
                    }

                    state = 3;
                } else if (state == 3) {
                    log("Checking item count");
                    checkBoxes(grandExchangeBoxStates);

                    if (items.size() != 0 && currentBoxID != -1) {
                        log("Items exists");
                        log(getGrandExchange().getStatus(boxes[currentBoxID]).toString());
                        Sleep.sleepUntil(() -> getGrandExchange().getStatus(boxes[currentBoxID]) == GrandExchange.Status.FINISHED_BUY, random(10000, 25000), 500);
                    } else state = 6;
                } else if (state == 4) {
                    log("Checking state 4");
                    checkBoxes(grandExchangeBoxStates);
                    if (state == 4) {

                        log("Checking state 4 is done");

                        int index = 0;

                        for (int j = 0; j < items.size(); j++) {
                            if (getGrandExchange().getItemId(boxes[currentBoxID]) == items.get(j).getId()) {
                                index = j;
                                break;
                            }
                        }

                        String name = items.get(index).getSearchTerm();
                        int id = getGrandExchange().getItemId(boxes[currentBoxID]);
                        int price = 0;
                        if (getGrandExchange().getItemPrice(boxes[currentBoxID]) < 1000) {
                            price = items.get(index).getPrice() + 5;
                            items.get(index).setPrice(price);
                        }

                        int amount = getGrandExchange().getAmountRemaining(boxes[currentBoxID]);

                        while (getGrandExchange().getStatus(boxes[currentBoxID]) != GrandExchange.Status.FINISHED_BUY) {

                            log("Aborting buy");

                            mouse.move(random(box_cords[currentBoxID][0][0], box_cords[currentBoxID][0][1]), random(box_cords[currentBoxID][1][0], box_cords[currentBoxID][1][1]));

                            sleep(random(500, 700));

                            Point mousePoint = mouse.getPosition();

                            mouse.click(true);

                            sleep(random(200, 400));

                            mouse.move((int) mousePoint.getX() + random(-50, 35), (int) mousePoint.getY() + random(34, 47));

                            sleep(random(500, 700));

                            mouse.click(false);

                            sleep(random(1500, 2000));
                        }

                        log("Collecting items from grand exchange to inventory");

                        getGrandExchange().collect(false);

                        sleep(random(700, 1500));

                        log("Buying item again");

                        getGrandExchange().buyItem(id, name, price, amount);

                        log("Starting sleep");

                        Sleep.sleepUntil(() -> getGrandExchange().getStatus(boxes[currentBoxID]) == GrandExchange.Status.FINISHED_BUY, random(10000, 25000), 500);

                        log("sleep done going to state 3");

                        state = 3;
                    }
                } else if (state == 5) {

                    log("State 5");

                    log("Collecting items from grand exchange to inventory");

                    getGrandExchange().collect(false);

                    sleep(random(500, 1000));

                    int itemID = 0;

                    for (int i = 0; i < items.size(); i++) {

                        for (GrandExchangeBoxState boxState : grandExchangeBoxStates) {
                            if (boxState.getSlot() == currentBoxID) itemID = boxState.getItemID();
                        }

                        if (itemID == items.get(i).getId()) {
                            log("Item bought, deleting");
                            items.remove(i);
                        }
                    }

                    log("going to state 3");

                    state = 3;
                } else if (state == 6) {

                    log("State 6");
                    log("Closing GE if open");

                    if (getGrandExchange().isOpen()) getGrandExchange().close();

                    log("Opening bank and depositing all");

                    bankManager.openBank();

                    getBank().depositAll();

                    Sleep.sleepUntil(() -> !getBank().contains("Coins"), 10000, 500);

                    goldAmount = getBank().getAmount("Coins");

                    return goldAmount;
                }
            }
            sleep(random(300, 500));
        }
    }

    private void checkBoxes(List<GrandExchangeBoxState> grandExchangeBoxStates) {
        for (GrandExchangeBoxState boxState : grandExchangeBoxStates) {
            if (boxState.getStatus().equals("PENDING_BUY") || boxState.getStatus().equals("COMPLETING_BUY")) {
                state = 4;
                currentBoxID = boxState.getSlot();
            } else if (boxState.getStatus().equals("FINISHED_BUY")) {
                state = 5;
                currentBoxID = boxState.getSlot();
            }
        }
    }
}
