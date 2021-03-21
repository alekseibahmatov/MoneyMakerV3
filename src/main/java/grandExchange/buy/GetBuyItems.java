package grandExchange.buy;

import models.BuyItem;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.script.MethodProvider;
import utils.GEPrice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetBuyItems extends MethodProvider {
    private String allNeededItemsNames[][][] = {
            {{"Pestle and mortar", "Unicorn horn"}},
            {{"Pestle and mortar", "Desert goat horn"}},
            {{"Chisel", "Uncut opal"}, {"Chisel", "Limestone"}},
            {{"Supercompost", "Volcanic ash"}},
            {{"Bullseye lantern (empty)", "Swamp tar", "Teleport to house", "Ring of wealth (5)"}, {"Falador teleport", "Ring of wealth (5)"}},
            {{"Ring of wealth (5)", "Games necklace(8)"}},
            {{"Guam leaf", "Vial of water"}, {"Raw bear meat", "Raw rat meat", "Raw beef", "Raw chicken"}},
            {{"Tarromin", "Vial of water"}, {"Guam potion (unf)", "Eye of newt"}},
            {{"Flax"}}
    };

    private String allCheckItemsNames[][][] = {
            {{"Pestle and mortar", "Unicorn horn"}},
            {{"Pestle and mortar", "Desert goat horn"}},
            {{"Chisel", "Uncut opal"}, {"Chisel", "Limestone"}},
            {{"Supercompost", "Volcanic ash"}},
            {{"Bullseye lantern (empty)", "Swamp tar", "Teleport to house", "Ring of wealth (5)"}, {"Falador teleport", "Ring of wealth (5)"}},
            {{"Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)", "Games necklace(1),Games necklace(2),Games necklace(3),Games necklace(4),Games necklace(5),Games necklace(6),Games necklace(7),Games necklace(8)"}},
            {{"Guam leaf", "Vial of water"}, {"Raw bear meat", "Raw rat meat", "Raw beef", "Raw chicken"}},
            {{"Tarromin", "Vial of water"}, {"Guam potion (unf)", "Eye of newt"}},
            {{"Flax"}}
    };

    private int allNeededItemsIDs[][][] = {
            {{233, 237}},
            {{233, 9735}},
            {{1755, 1625}, {1755, 3211}},
            {{6034, 21622}},
            {{4546, 1939, 8013, 11980}, {8009, 11980}},
            {{11980, 3853}},
            {{249, 227}, {2136, 2134, 2132, 2138}},
            {{253, 227}, {91, 221}},
            {{1779}}
    };

    private int allNeededItemsQuantity[][][] = {
            {{1, 1500}},
            {{1, 1500}},
            {{1, 300}, {1, 1500}},
            {{100, 200}},
            {{300, 300, 5, 1}, {1, 1}},
            {{1, 1}},
            {{500, 500}, {1, 1, 1, 1}},
            {{1500, 1500}, {60, 60}},
            {{10000}}
    };

    public List<BuyItem> getItems(int taskID, int step, Bank bank) throws IOException {
        List<BuyItem> items = new ArrayList<>();
        GEPrice priceGuide = new GEPrice();

        for (int i = 0; i < allNeededItemsIDs[taskID][step].length; i++) {

            String splittedItems[] = allCheckItemsNames[taskID][step][i].split(",");

            if (splittedItems.length == 1) {
                if (bank.getAmount(allNeededItemsNames[taskID][step][i]) == 0) {
                    BuyItem item = new BuyItem();

                    int price = priceGuide.getPrice(allNeededItemsIDs[taskID][step][i]).get();

                    item.setPrice(price);
                    item.setAmount(allNeededItemsQuantity[taskID][step][i]);
                    item.setId(allNeededItemsIDs[taskID][step][i]);
                    item.setSearchTerm(allNeededItemsNames[taskID][step][i].substring(0, random(allNeededItemsNames[taskID][step][i].length() / 2, allNeededItemsNames[taskID][step][i].length())));

                    items.add(item);
                }
            } else {
                boolean exists = false;

                for (int j = 0; j < splittedItems.length; j++) {
                    if (bank.getAmount(splittedItems[j]) != 0) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    BuyItem item = new BuyItem();

                    int price = priceGuide.getPrice(allNeededItemsIDs[taskID][step][i]).get();

                    item.setPrice(price);
                    item.setAmount(allNeededItemsQuantity[taskID][step][i]);
                    item.setId(allNeededItemsIDs[taskID][step][i]);
                    item.setSearchTerm(allNeededItemsNames[taskID][step][i].substring(0, random(allNeededItemsNames[taskID][step][i].length() / 2, allNeededItemsNames[taskID][step][i].length())));

                    items.add(item);
                }
            }
        }

        return items;
    }
}
