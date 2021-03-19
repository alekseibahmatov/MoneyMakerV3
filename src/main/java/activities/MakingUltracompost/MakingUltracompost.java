package activities.MakingUltracompost;

import activities.setup.Task;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class MakingUltracompost extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        return getInventory().contains("Supercompost") && getInventory().contains("Volcanic ash");
    }

    @Override
    public void execute(int type) {

        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();
        log("bank checked");

        new ConditionalSleep(60000, 250) {
            @Override
            public boolean condition() throws InterruptedException {
                return !getBank().isOpen();
            }
        }.sleep();

        log("bank closed");

        if (getInventory().contains("Supercompost")) getInventory().getItem("Supercompost").interact();
        Sleep.sleepUntil(random(300, 500));
        if (getInventory().contains("Volcanic ash")) getInventory().getItem("Volcanic ash").interact();
        Sleep.sleepUntil(random(300, 500));
        log("interaction between Supercompost and Volcanic ash");

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget compostWidget = getWidgets().get(270, 14);
                return (compostWidget != null && compostWidget.isVisible());
            }
        }.sleep();
        log("compostWidget registered");

        RS2Widget compostWidget = getWidgets().get(270, 14);

        if (compostWidget != null && compostWidget.isVisible()) {
            compostWidget.interact();
        }
        log("started interactions");
        Sleep.sleepUntil(random(500, 700));

        new ConditionalSleep(33000, 3000) {
            @Override
            public boolean condition() throws InterruptedException {
                RS2Widget compostWidget = getWidgets().get(270, 14);
                return !getInventory().contains("Supercompost") ||
                        !getInventory().contains("Volcanic ash") ||
                        (compostWidget != null && compostWidget.isVisible());
            }
        }.sleep();
        log("the end");

    }
}
