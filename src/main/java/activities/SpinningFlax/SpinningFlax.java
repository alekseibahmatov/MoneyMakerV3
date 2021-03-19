package activities.SpinningFlax;

import activities.setup.Task;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;
import utils.Sleep;

public class SpinningFlax extends Task {

    private Area lumbridgeBank = new Area((new Position(3207, 3220, 2)), new Position(3210, 3218, 2));

    private Area spinningWheelRoom = new Area((new Position(3208, 3213, 1)), new Position(3210, 3214, 1));

    @Override
    public boolean isNeededToStartAtGE() {
        return true;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);

        return lumbridgeBank.contains(myPosition());
    }

    @Override
    public void execute(int type) {

    }

    private void depositAll() {
        log("depositin all");
        if (!Banks.LUMBRIDGE_UPPER.contains(myPosition())) getWalking().webWalk(lumbridgeBank);
        else {
            if (!getBank().isOpen()) {
                try {
                    getBank().open();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getBank().depositAll();
                Sleep.sleepUntil(random(300, 500));
            }
        }
    }

    private void spinFlax() {
        log("spinning flax");
        if (!getInventory().contains("Flax")) {
            log("dont have enough flax");
            if (Banks.LUMBRIDGE_UPPER.contains(myPlayer())) {
                if (!getBank().isOpen()) {
                    try {
                        getBank().open();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log("got full inventory of flax");
                    getBank().withdrawAll("Flax");
                    Sleep.sleepUntil(random(300, 500));
                }
            }
        }
        if (!spinningWheelRoom.contains(myPosition())) {
            getWalking().webWalk(spinningWheelRoom);

            if (spinningWheelRoom.contains(myPosition())) {
                RS2Object spinningWheel = objects.closest("Spinning wheel");

                if (spinningWheel != null && spinningWheel.isVisible()) spinningWheel.interact();

                new ConditionalSleep(10000, 1000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        log("sleep until see spinWidget");
                        RS2Object spinningWheel = objects.closest("Spinning wheel");
                        if (spinningWheel != null && spinningWheel.isVisible()) spinningWheel.interact();
                        RS2Widget spinWidget = getWidgets().get(270, 16, 38);
                        return (spinWidget != null && spinWidget.isVisible());
                    }
                }.sleep();

                RS2Widget spinWidget = getWidgets().get(270, 16, 38);
                if (spinWidget != null && spinWidget.isVisible()) {
                    spinWidget.interact();
                }

                new ConditionalSleep(60000, 1000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        log("sleep until finish spinning");
                        RS2Widget fuckingDialog = getWidgets().get(217, 3);
                        RS2Widget lvlUpWidget = getWidgets().get(233, 3);
                        RS2Widget newAbilitiesWidget = getWidgets().get(11, 4);
                        return (newAbilitiesWidget != null && newAbilitiesWidget.isVisible()) ||
                                (lvlUpWidget != null && lvlUpWidget.isVisible()) ||
                                (fuckingDialog != null && fuckingDialog.isVisible()) ||
                                !getInventory().contains("Flax");
                    }
                }.sleep();

            }
        }
    }
}
