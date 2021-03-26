package grandExchange.buy;

import models.BuyItem;
import models.NeedToBuyItem;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.script.MethodProvider;
import utils.GEPrice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetBuyItems extends MethodProvider {

    private NeedToBuyItem[][][] needToBuyItems = {
            {
                    {
                            new NeedToBuyItem("Pestle and mortar", 233, 1, 1, "Pestle and mortar"),
                            new NeedToBuyItem("Unicorn horn", 237, 500, 1, "Unicorn horn"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Pestle and mortar", 233, 1, 1, "Pestle and mortar"),
                            new NeedToBuyItem("Desert goat horn", 9735, 500, 1, "Desert goat horn"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Chisel", 1755, 1, 1, "Chisel"),
                            new NeedToBuyItem("Uncut opal", 1625, 500, 1, "Uncut opal"),
                    },
                    {
                            new NeedToBuyItem("Chisel", 1755, 1, 1, "Chisel"),
                            new NeedToBuyItem("Limestone", 3211, 500, 1, "Limestone"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Supercompost", 6034, 100, 1, "Supercompost"),
                            new NeedToBuyItem("Volcanic ash", 21622, 200, 1, "Volcanic ash"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Bullseye lantern (empty)", 4546, 300, 1, "Bullseye lantern (empty)"),
                            new NeedToBuyItem("Volcanic ash", 1939, 300, 1, "Volcanic ash"),
                            new NeedToBuyItem("Teleport to house", 8013, 5, 1, "Teleport to house"),
                            new NeedToBuyItem("Ring of wealth (5)", 11980, 1, 1, "Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)"),
                    },
                    {
                            new NeedToBuyItem("Falador teleport", 8009, 1, 1, "Falador teleport"),
                            new NeedToBuyItem("Ring of wealth (5)", 11980, 1, 1, "Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Ring of wealth (5)", 11980, 1, 1, "Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)"),
                            new NeedToBuyItem("Games necklace(8)", 3853, 1, 1, "Games necklace(1),Games necklace(2),Games necklace(3),Games necklace(4),Games necklace(5),Games necklace(6),Games necklace(7),Games necklace(8)"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Guam leaf", 249, 500, 1, "Guam leaf"),
                            new NeedToBuyItem("Vial of water", 227, 500, 1, "Vial of water"),
                    },
                    {
                            new NeedToBuyItem("Raw bear meat", 2136, 1, 1, "Raw bear meat"),
                            new NeedToBuyItem("Raw rat meat", 2134, 1, 1, "Raw rat meat"),
                            new NeedToBuyItem("Raw beef", 2132, 1, 1, "Raw beef"),
                            new NeedToBuyItem("Raw chicken", 2138, 1, 1, "Raw chicken"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Tarromin", 253, 1500, 1, "Tarromin"),
                            new NeedToBuyItem("Vial of water", 227, 1500, 1, "Vial of water"),
                    },
                    {
                            new NeedToBuyItem("Guam potion (unf)", 91, 60, 1, "Guam potion (unf)"),
                            new NeedToBuyItem("Eye of newt", 221, 60, 1, "Eye of newt"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Flax", 1779, 10000, 1, "Flax"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Mind rune", 558, 500, 15, "Mind rune"),
                            new NeedToBuyItem("Water rune", 555, 500, 15, "Water rune"),
                            new NeedToBuyItem("Earth rune", 557, 500, 15, "Earth rune"),
                            new NeedToBuyItem("Fire rune", 554, 500, 15, "Fire rune"),
                            new NeedToBuyItem("Chaos rune", 562, 500, 15, "Chaos rune"),
                            new NeedToBuyItem("Tuna", 361, 500, 3, "Tuna"),
                            new NeedToBuyItem("Wooden shield", 1171, 1, 1, "Wooden shield"),
                            new NeedToBuyItem("Staff of air", 1381, 1, 1, "Staff of air"),
                    },
            },
    };

    public List<BuyItem> getItems(int taskID, int step, Bank bank) throws IOException {
        List<BuyItem> items = new ArrayList<>();
        GEPrice priceGuide = new GEPrice();

        for (int i = 0; i < needToBuyItems[taskID][step].length; i++) {

            String splittedItems[] = needToBuyItems[taskID][step][i].getCheckName().split(",");

            if (splittedItems.length == 1) {
                if (bank.getAmount(needToBuyItems[taskID][step][i].getName()) == 0 || bank.getAmount(needToBuyItems[taskID][step][i].getName()) < needToBuyItems[taskID][step][i].getItemMinQ()) {
                    BuyItem item = new BuyItem();

                    int price = priceGuide.getPrice(needToBuyItems[taskID][step][i].getItemID()).get();

                    item.setPrice(price);
                    item.setAmount(needToBuyItems[taskID][step][i].getItemQ());
                    item.setId(needToBuyItems[taskID][step][i].getItemID());
                    item.setSearchTerm(needToBuyItems[taskID][step][i].getName().substring(0, random(needToBuyItems[taskID][step][i].getName().length() / 2, needToBuyItems[taskID][step][i].getName().length())));

                    items.add(item);
                }
            } else {
                boolean exists = false;

                for (int j = 0; j < splittedItems.length; j++) {
                    if (bank.getAmount(splittedItems[j]) != 0 || bank.getAmount(splittedItems[j]) > needToBuyItems[taskID][step][i].getItemMinQ()) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    BuyItem item = new BuyItem();

                    int price = priceGuide.getPrice(needToBuyItems[taskID][step][i].getItemID()).get();

                    item.setPrice(price);
                    item.setAmount(needToBuyItems[taskID][step][i].getItemQ());
                    item.setId(needToBuyItems[taskID][step][i].getItemID());
                    item.setSearchTerm(needToBuyItems[taskID][step][i].getName().substring(0, random(needToBuyItems[taskID][step][i].getName().length() / 2, needToBuyItems[taskID][step][i].getName().length())));

                    items.add(item);
                }
            }
        }

        return items;
    }
}
