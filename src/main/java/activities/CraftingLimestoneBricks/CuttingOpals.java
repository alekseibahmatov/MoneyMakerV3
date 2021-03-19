package activities.CraftingLimestoneBricks;

import activities.setup.Task;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class CuttingOpals extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        return getInventory().contains("Uncut opal") && getInventory().contains("Chisel");
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

        if (getInventory().contains("Chisel")) getInventory().getItem("Chisel").interact();
        Sleep.sleepUntil(random(300, 500));
        if (getInventory().contains("Uncut opal")) getInventory().getItem("Uncut opal").interact();
        Sleep.sleepUntil(random(300, 500));
        log("interaction between chisel and uncut opal");

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget opalWidget = getWidgets().get(270, 14);
                return (opalWidget != null && opalWidget.isVisible());
            }
        }.sleep();
        log("opalWidget registered");

        RS2Widget opalWidget = getWidgets().get(270, 14);

        if (opalWidget != null && opalWidget.isVisible()) {
            opalWidget.interact("");
        }
        log("started interactions");
        Sleep.sleepUntil(random(500, 700));

        new ConditionalSleep(33000, 3000) {
            @Override
            public boolean condition() throws InterruptedException {
                RS2Widget lvlUpWidget = getWidgets().get(233, 3);
                RS2Widget fletchingWidget = getWidgets().get(229, 2);
                RS2Widget opalWidget = getWidgets().get(270, 14);
                RS2Widget newAbilitiesWidget = getWidgets().get(11, 4);

                if ((lvlUpWidget != null && lvlUpWidget.isVisible())) {
                    lvlUpWidget.interact();
                }

                if ((newAbilitiesWidget != null && newAbilitiesWidget.isVisible())) {
                    newAbilitiesWidget.interact();
                }

                if ((fletchingWidget != null && fletchingWidget.isVisible())) {
                    fletchingWidget.interact();
                }

                if ((opalWidget != null && opalWidget.isVisible())) {
                    opalWidget.interact();
                }

                return !getInventory().contains("Uncut opal");
            }
        }.sleep();
    }
}
