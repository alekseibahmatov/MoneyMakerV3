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

        log("benis opalz");
        return getInventory().contains("Uncut opal") && getInventory().contains("Chisel");
    }


    @Override
    public void execute(int type) {
        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();

        log("bank closed");

        if (getInventory().contains("Chisel")) getInventory().getItem("Chisel").interact();
        Sleep.sleepUntil(random(500, 1000));
        if (getInventory().contains("Uncut opal")) getInventory().getItem("Uncut opal").interact();
        Sleep.sleepUntil(random(500, 1000));

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget opalWidget = getWidgets().get(270, 14);
                if (opalWidget != null && opalWidget.isVisible()) {
                    opalWidget.interact();
                    return true;
                }
                return false;
            }
        }.sleep();
        log("interaction between chisel and uncut opal");

        new ConditionalSleep(40000, 1000) {
            @Override
            public boolean condition() {
                while (getDialogues().isPendingContinuation()) {
                    getDialogues().clickContinue();
                    return true;
                }
                return !getInventory().contains("Uncut opal");
            }
        }.sleep();
    }
}
