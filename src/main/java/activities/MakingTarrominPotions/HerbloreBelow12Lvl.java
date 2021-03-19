package activities.MakingTarrominPotions;

import activities.setup.Task;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class HerbloreBelow12Lvl extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        return getInventory().contains("Guam potion (unf)") && getInventory().contains("Eye of newt");
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

        if (getInventory().contains("Guam potion (unf)")) getInventory().getItem("Guam potion (unf)").interact();
        Sleep.sleepUntil(random(300, 500));
        if (getInventory().contains("Eye of newt")) getInventory().getItem("Eye of newt").interact();
        Sleep.sleepUntil(random(300, 500));
        log("interaction between Guam potion (unf) and Eye of newt");

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget attackWidget = getWidgets().get(270, 14);
                return (attackWidget != null && attackWidget.isVisible());
            }
        }.sleep();
        log("attackWidget registered");

        RS2Widget attackWidget = getWidgets().get(270, 14);

        if (attackWidget != null && attackWidget.isVisible()) {
            attackWidget.interact();
        }
        log("started interactions");
        Sleep.sleepUntil(random(500, 700));

        new ConditionalSleep(10000, 250) {
            @Override
            public boolean condition() throws InterruptedException {
                RS2Widget attackWidget = getWidgets().get(270, 14);
                RS2Widget lvlUpWidget = getWidgets().get(233, 3);
                RS2Widget newAbilitiesWidget = getWidgets().get(11, 4);
                return (newAbilitiesWidget != null && newAbilitiesWidget.isVisible()) ||
                        (lvlUpWidget != null && lvlUpWidget.isVisible()) ||
                        !getInventory().contains("Guam potion (unf)") ||
                        !getInventory().contains("Eye of newt") ||
                        (attackWidget != null && attackWidget.isVisible());
            }
        }.sleep();
    }
}
