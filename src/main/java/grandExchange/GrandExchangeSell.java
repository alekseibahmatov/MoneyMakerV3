package grandExchange;

import grandExchange.sell.GetSellItems;
import grandExchange.utils.GrandExchangeManager;
import models.GrandExchangeBoxState;
import models.SellItem;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;
import bank.utils.BankManager;
import utils.Sleep;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrandExchangeSell extends MethodProvider {
    private Area grandExchangeArea = new Area(3169, 3488, 3161, 3486);

    private GetSellItems getSellItems = new GetSellItems();

    GrandExchange.Box boxes[] = {
            GrandExchange.Box.BOX_1,
            GrandExchange.Box.BOX_2,
            GrandExchange.Box.BOX_3,
            GrandExchange.Box.BOX_4,
            GrandExchange.Box.BOX_5,
            GrandExchange.Box.BOX_6,
            GrandExchange.Box.BOX_7,
            GrandExchange.Box.BOX_8
    };

    int box_cords[][][] = {
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

    public long sellItems(int sellType, BankManager bankManager, GrandExchangeManager grandExchangeManager) throws IOException, InterruptedException {
        List<SellItem> items = null;

        long goldAmount;

        while(true) {
            if (!grandExchangeArea.contains(myPosition())) getWalking().webWalk(grandExchangeArea);
            else {
                List<GrandExchangeBoxState> grandExchangeBoxStates = new ArrayList<>();

                currentBoxID = -1;

                if(state > 2) {
                    List<Integer> ids = new ArrayList<>();

                    boolean isEmpty = false;

                    for (SellItem item : items) {
                        ids.add(item.getId());
                    }

                    for (int i = 0; i < 8; i++) {
                        String status = getGrandExchange().getStatus(boxes[i]).toString();

                        if (status.equals("PENDING_SALE") || status.equals("COMPLETING_SALE") || status.equals("FINISHED_SALE")) {

                            isEmpty = false;

                            int finalI = i;

                            boolean isIdExists = ids.stream().anyMatch(n -> {
                                if (n == getGrandExchange().getItemId(boxes[finalI])) return true;
                                return false;
                            });

                            if (!isIdExists) {
                                SellItem item = new SellItem(getGrandExchange().getItemId(boxes[i]), getGrandExchange().getItemPrice(boxes[i]), getGrandExchange().getAmountRemaining(boxes[i]));

                                items.add(item);
                            }

                            GrandExchangeBoxState newState = new GrandExchangeBoxState();

                            newState.setStatus(status);
                            newState.setItemID(getGrandExchange().getItemId(boxes[i]));
                            newState.setSlot(i);

                            grandExchangeBoxStates.add(newState);
                        } else if (status.equals("EMPTY")) {
                            isEmpty = true;
                        }
                    }
                }

                if(state == 1) {
                    bankManager.openBank();

                    getBank().depositAll();

                    items = getSellItems.getItems(getBank());

                    sleep(random(500, 700));

                    getBank().close();

                    state = 2;
                }

                else if(state == 2) {
                    grandExchangeManager.openGE();

                    for (SellItem item : items) {
                        getGrandExchange().sellItem(item.getId(), item.getPrice(), (int) item.getQuantity());
                        sleep(random(500, 1000));
                    }

                    state = 3;
                }

                else if(state == 3) {
                    checkBoxes(grandExchangeBoxStates);
                    if(items.size() != 0 && currentBoxID != -1) {
                        Sleep.sleepUntil(() -> getGrandExchange().getStatus(boxes[currentBoxID]) == GrandExchange.Status.FINISHED_SALE, random(10000, 25000), 500);
                    } else state = 6;
                }

                else if(state == 4) {
                    checkBoxes(grandExchangeBoxStates);
                    if(state == 4) {
                        int index = 0;

                        for(int j = 0; j < items.size(); j++) {
                            if(getGrandExchange().getItemId(boxes[currentBoxID]) == items.get(j).getId()) {
                                index = j;
                                break;
                            }
                        }

                        int id = getGrandExchange().getItemId(boxes[currentBoxID]);
                        int price = 0;
                        if (getGrandExchange().getItemPrice(boxes[currentBoxID]) < 1000) {
                            price = items.get(index).getPrice() - 3;
                            items.get(index).setPrice(price);
                        }

                        int amount = getGrandExchange().getAmountRemaining(boxes[currentBoxID]);

                        while (getGrandExchange().getStatus(boxes[currentBoxID]) != GrandExchange.Status.FINISHED_SALE) {
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

                        getGrandExchange().collect(false);

                        sleep(random(700, 1500));

                        getGrandExchange().sellItem(id, price, amount);

                        Sleep.sleepUntil(() -> getGrandExchange().getStatus(boxes[currentBoxID]) == GrandExchange.Status.FINISHED_SALE, random(10000, 25000), 500);

                        state = 3;
                    }
                }

                else if(state == 5) {
                    getGrandExchange().collect(false);

                    sleep(random(500, 1000));

                    int itemID = 0;

                    for(int i = 0; i < items.size(); i++) {

                        for (GrandExchangeBoxState boxState : grandExchangeBoxStates) {
                            if(boxState.getSlot() == currentBoxID) itemID = boxState.getItemID();
                        }

                        if(itemID == items.get(i).getId()) {
                            items.remove(i);
                        }
                    }

                    state = 3;
                }
                else if(state == 6) {
                    bankManager.openBank();

                    getBank().depositAll();

                    if(sellType == 1) {
                        goldAmount = getBank().getAmount("Coins");

                        return goldAmount;
                    }

                    else if(sellType == 2) {
                        goldAmount = -999;

                        return goldAmount;
                    }
                }
            }
        }
    }

    private void checkBoxes(List<GrandExchangeBoxState> grandExchangeBoxStates) {
        for (GrandExchangeBoxState boxState : grandExchangeBoxStates) {
            if(boxState.getStatus().equals("PENDING_SALE") || boxState.getStatus().equals("COMPLETING_SALE")) {
                state = 4;
                currentBoxID = boxState.getSlot();
            }
            else if(boxState.getStatus().equals("FINISHED_SALE")) {
                state = 5;
                currentBoxID = boxState.getSlot();
            }
        }
    }
}
