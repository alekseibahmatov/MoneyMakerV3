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
                            new NeedToBuyItem("Unicorn horn", 237, random(476, 639), random(100, 200), "Unicorn horn"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Pestle and mortar", 233, 1, 1, "Pestle and mortar"),
                            new NeedToBuyItem("Desert goat horn", 9735, random(429, 658), random(100, 200), "Desert goat horn"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Chisel", 1755, 1, 1, "Chisel"),
                            new NeedToBuyItem("Uncut opal", 1625, random(489, 672), random(100, 200), "Uncut opal"),
                    },
                    {
                            new NeedToBuyItem("Chisel", 1755, 1, 1, "Chisel"),
                            new NeedToBuyItem("Limestone", 3211, random(462, 592), random(100, 200), "Limestone"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Supercompost", 6034, random(455, 613), 11, "Supercompost"),
                            new NeedToBuyItem("Volcanic ash", 21622, random(422, 666), 22, "Volcanic ash"),
                    },
            },
//            {
//                    {
//                            new NeedToBuyItem("Bullseye lantern (empty)", 4546, 300, 1, "Bullseye lantern (empty)"),
//                            new NeedToBuyItem("Volcanic ash", 1939, 300, 1, "Volcanic ash"),
//                            new NeedToBuyItem("Teleport to house", 8013, 5, 1, "Teleport to house"),
//                            new NeedToBuyItem("Ring of wealth (5)", 11980, 1, 1, "Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)"),
//                    },
//                    {
//                            new NeedToBuyItem("Falador teleport", 8009, 1, 1, "Falador teleport"),
//                            new NeedToBuyItem("Ring of wealth (5)", 11980, 1, 1, "Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)"),
//                    },
//            },
            {
                    {
                            new NeedToBuyItem("Ring of wealth (5)", 11980, 1, 1, "Ring of wealth (1),Ring of wealth (2),Ring of wealth (3),Ring of wealth (4),Ring of wealth (5)"),
                            new NeedToBuyItem("Games necklace(8)", 3853, 1, 1, "Games necklace(1),Games necklace(2),Games necklace(3),Games necklace(4),Games necklace(5),Games necklace(6),Games necklace(7),Games necklace(8)"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Guam leaf", 249, random(477, 677), 1, "Guam leaf"),
                            new NeedToBuyItem("Vial of water", 227, random(488, 688), 50, "Vial of water"),
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
                            new NeedToBuyItem("Tarromin", 253, random(499, 699), 1, "Tarromin"),
                            new NeedToBuyItem("Vial of water", 227, random(464, 686), 50, "Vial of water"),
                    },
                    {
                            new NeedToBuyItem("Guam potion (unf)", 91, random(45, 72), 1, "Guam potion (unf)"),
                            new NeedToBuyItem("Eye of newt", 221, random(35, 69), 1, "Eye of newt"),
                    },
            },
            {
                    {
                            new NeedToBuyItem("Flax", 1779, random(3124, 5451), random(100, 200), "Flax"),
                    },
                    {
                            new NeedToBuyItem("Chisel", 1755, 1, 1, "Chisel"),
                            new NeedToBuyItem("Uncut opal", 1625, random(422, 667), random(100, 200), "Uncut opal"),
                    }
            },
            {
                    {
                            new NeedToBuyItem("Mind rune", 558, random(422, 666), 41, "Mind rune"),
                            new NeedToBuyItem("Water rune", 555, random(422, 666), 23, "Water rune"),
                            new NeedToBuyItem("Earth rune", 557, random(422, 666), 61, "Earth rune"),
                            new NeedToBuyItem("Fire rune", 554, random(422, 666), 44, "Fire rune"),
                            new NeedToBuyItem("Chaos rune", 562, random(422, 666), 81, "Chaos rune"),
                            new NeedToBuyItem("Tuna", 361, random(422, 666), 3, "Tuna"),
                            new NeedToBuyItem("Wooden shield", 1171, 1, 1, "Wooden shield"),
                            new NeedToBuyItem("Staff of air", 1381, 1, 1, "Staff of air"),
                    },
                    {
                            new NeedToBuyItem("Law rune", 563, 500, 50, "Law rune"),
                            new NeedToBuyItem("Restore potion(4)", 2430, 100, 15, "Restore potion(4)"),
                            new NeedToBuyItem("Zamorak monk top", 1035, 1, 1, "Zamorak monk top"),
                            new NeedToBuyItem("Zamorak monk bottom", 1033, 1, 1, "Zamorak monk bottom"),
                            new NeedToBuyItem("Staff of air", 1381, 1, 1, "Staff of air"),
                            new NeedToBuyItem("Tuna", 361, 500, 3, "Tuna"),
                    }
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

                    int price = 0;

                    if(item.getPrice() != -1) item.setPrice(priceGuide.getPrice(needToBuyItems[taskID][step][i].getItemID()).get());

                    item.setPrice(item.getPrice());
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

                    int price = 0;

                    if(item.getPrice() != -1) item.setPrice(priceGuide.getPrice(needToBuyItems[taskID][step][i].getItemID()).get());

                    item.setPrice(item.getPrice());
                    item.setAmount(needToBuyItems[taskID][step][i].getItemQ());
                    item.setId(needToBuyItems[taskID][step][i].getItemID());
                    item.setSearchTerm(needToBuyItems[taskID][step][i].getName().substring(0, random(needToBuyItems[taskID][step][i].getName().length() / 2, needToBuyItems[taskID][step][i].getName().length())));

                    items.add(item);
                }
            }
        }

        return items;
    }

    public int setItemPrice(int taskID, int step, int itemID, int addToPrice) {
        NeedToBuyItem[] items = needToBuyItems[taskID][step];

        for (NeedToBuyItem item : items) {
            if (item.getItemID() == itemID) {
                int newPrice = item.getItemPrice() + addToPrice;
                item.setItemPrice(newPrice);
                return newPrice;
            }
        }
        return -1;
    }

    public BuyItem getItem(int taskID, int step, int itemID) {
        NeedToBuyItem[] items = needToBuyItems[taskID][step];
        for (NeedToBuyItem item : items) {
            if (item.getItemID() == itemID) {
                BuyItem buyItem = new BuyItem();

                buyItem.setPrice(item.getItemPrice());
                buyItem.setAmount(item.getItemQ());
                buyItem.setId(item.getItemID());
                buyItem.setSearchTerm(item.getName().substring(0, random(item.getName().length() / 2, item.getName().length())));

                return buyItem;
            }
        }
        return null;
    }
}
