package activities.MakingTarrominPotions;

import activities.setup.Task;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class MakingTarrominPotions extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        return getInventory().contains("Tarromin") && getInventory().contains("Vial of water");
    }

    @Override
    public void execute(int type) {
        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();

        new ConditionalSleep(60000, 250) {
            @Override
            public boolean condition() throws InterruptedException {
                return !getBank().isOpen();
            }
        }.sleep();

        log("bank closed");

        if (getInventory().contains("Tarromin")) getInventory().getItem("Tarromin").interact();
        Sleep.sleepUntil(random(300, 500));
        if (getInventory().contains("Vial of water")) getInventory().getItem("Vial of water").interact();
        Sleep.sleepUntil(random(300, 500));
        log("interaction between Tarromin and Vial of water");

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget tarrominWidget = getWidgets().get(270, 14);
                return (tarrominWidget != null && tarrominWidget.isVisible());
            }
        }.sleep();
        log("tarrominWidget registered");

        RS2Widget tarrominWidget = getWidgets().get(270, 14);

        if (tarrominWidget != null && tarrominWidget.isVisible()) {
            tarrominWidget.interact();
        }
        log("started interactions");

        new ConditionalSleep(30000, 1000) {
            @Override
            public boolean condition() throws InterruptedException {
                return !getInventory().contains("Vial of water") || !getInventory().contains("Tarromin");
            }
        }.sleep();
    }
}
