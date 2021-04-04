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
        return false;
    }

    @Override
    public boolean validate() {
        if (!getTabs().getOpen().equals(Tab.INVENTORY)) getTabs().open(Tab.INVENTORY);
        log("benis spinning flex");

        return getInventory().contains("Flax");
    }

    @Override
    public void execute(int type) {
        if (getBank().isOpen()) getBank().close();
        if (getGrandExchange().isOpen()) getGrandExchange().close();

        spinFlax();
    }

    private void spinFlax() {
        while (!spinningWheelRoom.contains(myPosition())) getWalking().webWalk(spinningWheelRoom);

        new ConditionalSleep(40000, 2000) {
            @Override
            public boolean condition() throws InterruptedException {
                if (objects.closest("Spinning wheel") != null && objects.closest("Spinning wheel").isVisible()) {
                    getCamera().toEntity(objects.closest("Spinning wheel"));
                    Sleep.sleepUntil(random(300, 500));
                    objects.closest("Spinning wheel").interact();
                    Sleep.sleepUntil(random(300, 500));
                    if (getWidgets().get(270, 16, 38) != null && getWidgets().get(270, 16, 38).isVisible()) {
                        getWidgets().get(270, 16, 38).interact();
                        log("started interactions");
                        return true;
                    }
                } else {
                    getCamera().moveYaw(random(160, 216));
                    Sleep.sleepUntil(random(300, 500));
                    getCamera().movePitch(random(53, 67));
                    log("moved camera");
                }
                return false;
            }
        }.sleep();

        new ConditionalSleep(60000, 1000) {
            @Override
            public boolean condition() throws InterruptedException {
                while (getDialogues().isPendingContinuation()) {
                    getDialogues().clickContinue();
                    return true;
                }
                return (!getInventory().contains("Flax") || myPlayer().isUnderAttack());
            }
        }.sleep();


    }
}
