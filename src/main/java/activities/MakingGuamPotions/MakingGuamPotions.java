package activities.MakingGuamPotions;

import activities.setup.Task;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class MakingGuamPotions extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);
        log("guam bear potions");

        return getInventory().contains("Guam leaf") && getInventory().contains("Vial of water");
    }

    @Override
    public void execute(int type) {
        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();

        if (getInventory().contains("Guam leaf")) getInventory().getItem("Guam leaf").interact();
        Sleep.sleepUntil(random(300, 500));
        if (getInventory().contains("Vial of water")) getInventory().getItem("Vial of water").interact();
        Sleep.sleepUntil(random(300, 500));
        log("interaction between Guam leaf and Vial of water");

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget guamWidget = getWidgets().get(270, 14);
                return (guamWidget != null && guamWidget.isVisible());
            }
        }.sleep();
        log("guamWidget registered");

        RS2Widget guamWidget = getWidgets().get(270, 14);

        if (guamWidget != null && guamWidget.isVisible()) {
            guamWidget.interact();
        }
        log("started interactions");
        Sleep.sleepUntil(random(500, 700));

        new ConditionalSleep(10000, 250) {
            @Override
            public boolean condition() throws InterruptedException {
                return !getInventory().contains("Vial of water") || !getInventory().contains("Guam leaf");
            }
        }.sleep();
    }
}
