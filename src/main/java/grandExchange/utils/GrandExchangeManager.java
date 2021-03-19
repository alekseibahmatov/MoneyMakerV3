package grandExchange.utils;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

import java.util.Random;

public class GrandExchangeManager extends MethodProvider {
    public void openGE() {

        if(getGrandExchange().isOpen()) return;

        if(getInventory().isItemSelected()) getInventory().deselectItem();

        Filter<RS2Object> geBoothFilter = rs2Object -> rs2Object.hasAction("Exchange");

        RS2Object geBooth = getObjects().closest(geBoothFilter);
        NPC exchangeWorker = getNpcs().closest("Grand Exchange Clerk");

        int random = new Random().nextInt(10);
        if (geBooth != null && random < 5) {
            getCamera().toEntity(geBooth);
            geBooth.interact("Exchange");
            new ConditionalSleep(2500, 3000) {
                @Override
                public boolean condition() {
                    return getGrandExchange().isOpen();
                }
            }.sleep();
        }

        if (exchangeWorker != null && random >= 5) {
            getCamera().toEntity(exchangeWorker);
            exchangeWorker.interact("Exchange");
            new ConditionalSleep(2500, 3000) {
                @Override
                public boolean condition() {
                    return getGrandExchange().isOpen();
                }
            }.sleep();
        }

        if (!getGrandExchange().isOpen()) openGE();
    }
}
