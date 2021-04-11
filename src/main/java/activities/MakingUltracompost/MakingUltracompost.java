package activities.MakingUltracompost;

import activities.setup.Task;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class MakingUltracompost extends Task {
    @Override
    public boolean isNeededToStartAtGE() {
        return false;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);
        log("benis ultra govno compost");

        return getInventory().contains("Supercompost") && (getInventory().contains("Volcanic ash") && getInventory().getAmount("Volcanic ash") > 1);
    }

    @Override
    public void execute(int type) {

        if (getBank().isOpen()) getBank().close();
        else if (getGrandExchange().isOpen()) getGrandExchange().close();

        log("bank closed");

        if (getInventory().contains("Supercompost")) getInventory().getItem("Supercompost").interact();
        Sleep.sleepUntil(random(300, 500));
        if (getInventory().contains("Volcanic ash")) getInventory().getItem("Volcanic ash").interact();

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() {
                RS2Widget compostWidget = getWidgets().get(270, 14);
                if (compostWidget != null && compostWidget.isVisible()) {
                    compostWidget.interact();
                    return true;
                }
                return false;
            }
        }.sleep();
        log("interaction between supercompost and volcanic ash");

        new ConditionalSleep(33000, 1000) {
            @Override
            public boolean condition() {
                while (getDialogues().isPendingContinuation()) {
                    getDialogues().clickContinue();
                    return true;
                }
                return !getInventory().contains("Supercompost") ||
                        !getInventory().contains("Volcanic ash");

            }
        }.sleep();
        log("the end");

    }
}
