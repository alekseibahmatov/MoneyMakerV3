package grandExchange.sell;

import models.SellItem;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.script.MethodProvider;
import utils.GEPrice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetSellItems extends MethodProvider {

    String itemNames[] = {
            "Unicorn horn dust",
            "Goat horn dust",
            "Opal",
            "Limestone brick",
            "Ultracompost",
            "Bullseye lantern",
            "Ring of wealth",
            "Plank",
            "Attack potion(3)",
            "Guam potion (unf)",
            "Tarromin potion (unf)"
    };



    int notesItemIDs[] = {
            236,
            9737,
            1626,
            3212,
            21484,
            4547,
            2573,
            961,
            1778,
            122,
            92,
            96,
    };

    int itemIDs[] = {
            235,
            9736,
            1625,
            3211,
            21483,
            4546,
            2572,
            960,
            1777,
            121,
            91,
            95,
    };

    public List<SellItem> getItems(Bank bank) throws IOException, InterruptedException {
        List<SellItem> items = new ArrayList<>();
        GEPrice priceGuide = new GEPrice();

        bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);

        for (int i = 0; i < itemNames.length; i++) {
            if (bank.contains(itemNames[i])) {
                int price = Integer.valueOf(priceGuide.getPrice(itemIDs[i]).get());
                long quantity = bank.getAmount(itemIDs[i]);

                SellItem item = new SellItem(notesItemIDs[i], price, quantity);

                items.add(item);

                bank.withdrawAll(itemNames[i]);

                sleep(random(500, 900));
            }
        }
        return items;
    }
}
